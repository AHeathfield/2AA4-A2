package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import static ca.mcmaster.se2aa4.island.teamXXX.LoggerUtil.logger;
import org.json.JSONObject;

// This will be the "brain", it will take current data and determine next moves
public class RescueComputer implements Computer {
    private Map<Position, JSONObject> islandMap;    // JSON should only be response from SCAN
    private Position dronePos;
    private Direction droneDir;
    private Integer droneBat;
    private Instruction nextInstruction;

    private State currentState;

    public RescueComputer(JSONObject info) {
        logger.info(info.getString("heading"));
        this.droneDir = stringToDirection(info.getString("heading"));
        logger.info("droneDir: ", this.droneDir);
        this.droneBat = info.getInt("budget");
        logger.info("The drone is facing {}", droneDir.toString());
        logger.info("Battery level is {}", droneBat);

        // this.instructHistory = new Stack<>();
        this.dronePos = new Position(1, 1);     // I'm not sure if info contains this but it starts at 1,1
        this.currentState = new SearchState(this);  // First sequence after scan should be a search
        this.nextInstruction = new Instruction(Action.SCAN, new JSONObject());
        this.islandMap = new HashMap<>();
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

        // This lets the state pattern determine the next instruction based on current state
        nextInstruction = currentState.determineNextInstruction(droneResponse);
    }

    @Override
    public Instruction getNextInstruction() {
        // I believe nextInstruction is properly handled by the state pattern
        return nextInstruction;
    } 

    // Might put the next 2 methods in interface?
    // Leaky abstractions?? Its ok lol
    // Getters
    public Direction getDroneDirection() { return droneDir; }
    public Position getDronePosition() { return dronePos; } 
    public JSONObject getMapValueAtPosition(Position position) { return islandMap.get(position); }

    // Setters
    private void updateMapAtPosition(Position position, JSONObject landInfo) {
        islandMap.put(position, landInfo);
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
    }

    private Direction stringToDirection(String string) {
        for (Direction dir : Direction.values()){
            if (dir.toString().equals(string)){
                return dir;
            }
        }
        return null;
    }
}
