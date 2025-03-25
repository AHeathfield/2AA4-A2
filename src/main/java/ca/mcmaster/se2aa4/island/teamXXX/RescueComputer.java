package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static eu.ace_design.island.runner.Runner.run;
import org.json.JSONArray;

import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.States.*;
import org.json.JSONObject;

// This will be the "brain", it will take current data and determine next moves
public class RescueComputer implements Computer {
    private final Logger logger = LogManager.getLogger();
    private Map<Position, JSONObject> islandMap;    // JSON should only be response from SCAN
    private Position dronePos;
    private Direction droneDir;
    private Integer droneBat;
    private Instruction nextInstruction;
    private int crewMembers;
    private Map<Position, Integer> overwritten = new HashMap<>();
    private boolean interlaceScanning = false;
    private boolean stopOnCreek = false;

    private Position emergencySite;
    private Set<Position> creeksFound;
    private Position nearestCreek;

    private State currentState;

    public RescueComputer(JSONObject info) {
        logger.info(info.getString("heading"));
        this.droneDir = stringToDirection(info.getString("heading"));
        logger.info("droneDir: ", this.droneDir);
        this.droneBat = info.getInt("budget");
        logger.info("The drone is facing {}", droneDir.toString());
        logger.info("Battery level is {}", droneBat);
        this.crewMembers = info.getInt("men");

        // this.instructHistory = new Stack<>();
        this.dronePos = new Position(1, 1);     // I'm not sure if info contains this but it starts at 1,1
        // this.currentState = new SearchState(this);  
        this.currentState = new SearchState(this); // First sequence after scan should be a search
        this.nextInstruction = new Instruction(Action.SCAN, new JSONObject());
        this.islandMap = new HashMap<>();
        this.emergencySite = null;
        this.creeksFound = new HashSet<>();
    }

    // Idea is have processData save the values that should be saved each time and let
    // the state machine handle getting instructions
    @Override
    public void processData(JSONObject droneResponse) {
        logger.info("** Response received:\n"+droneResponse.toString(2));

        // handle cost
        Integer cost = droneResponse.getInt("cost");
        this.droneBat -= cost;
        logger.info("The cost of the action was {}", cost);

        

        // Updating map (note nextInstruction currently is the prev one)
        if (nextInstruction.getAction() == Action.SCAN) {
            updateMapAtPosition(dronePos, droneResponse);
        }
        else if (nextInstruction.getAction() == Action.FLY) {
            setDronePosition(dronePos.getPosition(droneDir, Turn.FORWARDS));
        }
        else if (nextInstruction.getAction() == Action.HEADING) {
            String direction = nextInstruction.getParameters().getString("direction");
            if (direction.equals(droneDir.getLeftDirection().toString())) {
                setDronePosition(dronePos.getPosition(droneDir, Turn.LEFT));
            } else {
                setDronePosition(dronePos.getPosition(droneDir, Turn.RIGHT));
            }
        }


        // This lets the state pattern determine the next instruction based on current state
        nextInstruction = currentState.determineNextInstruction(droneResponse);
    }

    @Override
    public Instruction getNextInstruction() {
        
        logger.info("drone position: ({}, {})", dronePos.x, dronePos.y);
        return nextInstruction;
    } 

    public int getDroneBattery() { return droneBat; }
    public Direction getDroneDirection() { return droneDir; }
    public Position getDronePosition() { return dronePos; } 
    public JSONObject getMapValueAtPosition(Position position) { return islandMap.get(position); }

    private boolean isCurrentPositionMarked() { return islandMap.containsKey(dronePos); }
    public boolean isMapPositionMarked(Position pos) { return islandMap.containsKey(pos); }

    // Setters
    private void updateMapAtPosition(Position position, JSONObject landInfo) {
        if (creeksFound.contains(position)) {
            logger.info("creek already found at position ({}, {})", position.x, position.y);
            overwritten.putIfAbsent(position, 1);
            overwritten.put(position, overwritten.get(position) + 1);
            return;
        }

        islandMap.put(position, landInfo);
        if (landInfo.getJSONObject("extras").getJSONArray("creeks").length() > 0) {
            logger.info("adding creek");
            addCreekFound(position);
        }
    }
    public void setCurrentState(State state) {
        currentState = state;
    }
    public void setDroneDirection(Direction dir) {
        droneDir = dir;
    }
    public void setDronePosition(Position pos) {
        dronePos = pos;
    }
    public void setEmergencySite(Position pos) {
        emergencySite = pos;
    }

    public void addCreekFound(Position pos) {
        creeksFound.add(pos);
    }

    public boolean isCreek(Position pos) {
        return creeksFound.contains(pos);
    }

    // Displays the map for debugging!
    public void displayIslandMap() {
        logger.info("Map size: {}", islandMap.size());
        for (Map.Entry<Position, JSONObject> entry : islandMap.entrySet()) {
            Position position = entry.getKey();
            JSONObject jsonObject = entry.getValue();

            // Log the Position (key) and JSONObject (value)
            logger.info("Position: ({}, {}), Value: {}", position.x, position.y, jsonObject.toString());
            // logger.info("Value: " + jsonObject.toString());
            // System.out.println(); // Add a blank line for better readability
        }
        logger.info(overwritten.toString());
    }

    public void displayCreeks() {
        logger.info("Creeks found: ");
        for (Position creek : creeksFound) {
            logger.info("Position: ({}, {})", creek.x, creek.y);
        }
    }

    public Direction stringToDirection(String string) {
        for (Direction dir : Direction.values()){
            if (dir.toString().equals(string)){
                return dir;
            }
        }
        return null;
    }

    public Instruction calcNearestCreekToSite() {
        if (emergencySite == null) {
            logger.info("No emergency site found");
            return new Instruction(Action.STOP);
        }

        if (creeksFound.isEmpty()) {
            logger.info("No creeks found. Continue searching");
            stopOnCreek = true;
            setCurrentState(new CoastScanState(this));
            return new Instruction(Action.FLY);
        }

        Position nearestCreek = null;
        double shortestDistance = Double.MAX_VALUE;

        // go through all creeks and find creek position with shortest distance to site
        for (Position creek : creeksFound) {
            double distance = calculateDistance(emergencySite, creek);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestCreek = creek;
            }
        }

        this.nearestCreek = nearestCreek;
        logger.info("Nearest creek is at position ({}, {})", nearestCreek.x, nearestCreek.y);
        return new Instruction(Action.STOP);
    }

    public Position getNearestCreekPosition() {
        return nearestCreek;
    }

    private double calculateDistance(Position pos1, Position pos2) {
        int dx = pos1.x - pos2.x;
        int dy = pos1.y - pos2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getAvailableCrewMembers() {
        return crewMembers - 1;
    }

    public String getCreekId(Position pos) {
        if (creeksFound.contains(pos)) {
            JSONArray creeks = islandMap.get(pos).getJSONObject("extras").getJSONArray("creeks");
            return creeks.getString(0);
        }
        logger.info("no creek found at position ({}, {})", pos.x, pos.y);
        return null;
    }

    public void setInterlaceScanning() {
        interlaceScanning = true;
    }

    public boolean isInterlaceScanning() {
        return interlaceScanning;
    }

    public boolean stopOnCreek() {
        return stopOnCreek;
    }
}
