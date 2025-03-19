package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

// This will be the "brain", it will take current data and determine next moves
public class RescueComputer implements Computer {
    // TODO add members to store data about drone and about island (map?)
    private final Logger logger = LogManager.getLogger();
    private Direction droneDir;
    private Integer droneBat;
    private int counter;
    private boolean islandFound;
    private Instruction lastInstruction;
    private Instruction nextInstruction;
    private int distanceToIsland;

    public RescueComputer(JSONObject info) {
        logger.info(info.getString("heading"));
        this.droneDir = stringToDirection(info.getString("heading"));
        logger.info("droneDir: ", this.droneDir);
        this.droneBat = info.getInt("budget");
        logger.info("The drone is facing {}", droneDir.toString());
        logger.info("Battery level is {}", droneBat);
        this.counter = 0;
        this.distanceToIsland = 0;
        this.islandFound = false;
    }

    @Override
    public void processData(JSONObject droneResponse) {
        logger.info("** Response received:\n"+droneResponse.toString(2));

        // handle cost
        Integer cost = droneResponse.getInt("cost");
        this.droneBat -= cost;
        logger.info("The cost of the action was {}", cost);

        // 
        JSONObject param = new JSONObject();
        switch (lastInstruction.getAction()) {
            case ECHO:
                int range = droneResponse.getJSONObject("extras").getInt("range");
                if (!islandFound) {
                    // We found Island!
                    if (range != 0) {
                        islandFound = true;
                        distanceToIsland = range;
                        logger.info("island found in echo!");
                        logger.info("island found at " + distanceToIsland + "tiles away");
                        String islandDir = lastInstruction.getParameters().getString("direction");

                        // if we are already facing in the direction of the found island we just need to fly, else we need to turn to the direction
                        if (droneDir.toString() == islandDir) {
                            nextInstruction = new Instruction(Action.FLY, param);
                        } else {
                            param.put("direction", islandDir); // might be wrong
                            nextInstruction = new Instruction(Action.HEADING, param);
                        }   
                    } else {
                        // scanning all directions
                        logger.info("echo in all directions");
                        // you knwo what i mean uwu 
                        switch (counter) {
                            case 0:
                                param.put("direction", droneDir.toString());
                                nextInstruction = new Instruction(Action.ECHO, param);
                                logger.info("echo straight");
                                break;
                            case 1:
                                param.put("direction", droneDir.getRightDirection().toString());
                                nextInstruction = new Instruction(Action.ECHO, param);
                                logger.info("echo right");
                                break;
                            case 2:
                                nextInstruction = new Instruction(Action.FLY, param);
                                logger.info("scanned all dirs, go forward");
                                break;
                        }
                        counter++;
                        counter = counter % 3;
                        
                    }
                } else { // island already found before, move towards island
                    logger.info("island already found, echoing again");
                    // // If has checked all directions
                    // counter++;
                    // if (counter == 0) {
                    //     nextInstruction = new Instruction(Action.FLY, param);
                    // } else {
                    //     // Heading right from current direction
                    //     param.put("direction", droneDir.getRightDirection().toString());
                    // }
                }
                break;
            case SCAN:
                break;
            case FLY:
                if (!islandFound) {
                    counter = 0;
                    param.put("direction", droneDir.getLeftDirection().toString());
                    nextInstruction = new Instruction(Action.ECHO, param);
                    logger.info("echo left");
                } else {
                    logger.info("flying to island");
                    distanceToIsland--;
                    logger.info("distance: " + distanceToIsland);
                    if (distanceToIsland > 0) {
                        nextInstruction = new Instruction(Action.FLY, param);
                    } else {    // <= 0
                        nextInstruction = new Instruction(Action.STOP, param);
                    }
                }
                break;
            case HEADING:
                if (islandFound) {
                    distanceToIsland--;
                    nextInstruction = new Instruction(Action.FLY, param);
                }
                break;
            case STOP:
                logger.info("drone stopped");
                break;
            case LAND:
                break;

            
        }


        // TODO need to make this more flexible, currently hard coded for only stop
        // logger.info("** Response received:\n"+droneResponsroneResponse.toString(2));
        // Integer cost = droneResponse.getInt("cost");
        // this.droneBat -= cost;
        // logger.info("The cost of the action was {}", cost);
        // logger.info("Current Drone Battery is {}", this.droneBat);
        // this.droneStatus = droneResponse.getString("status");
        // logger.info("The status of the drone is {}", droneStatus);
        // JSONObject extraInfo = droneResponse.getJSONObject("extras");
        // logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public Instruction getNextInstruction() {
        
        lastInstruction = nextInstruction;

        if (nextInstruction == null) {
            JSONObject param = new JSONObject();
            param.put("direction", droneDir.getLeftDirection().toString());
            lastInstruction = new Instruction(Action.ECHO, param);
            return lastInstruction;
        }
        return nextInstruction;
        //find land with echo
        
        // counter = counter % 3;
        // lastAction = 
        //return new Instruction(Action.ECHO, params);

        //soon to use this
        //return new Instruction(Action.STOP, new JSONObject());
    } 

    //switch case OK because nothing else will ever be added

    private Direction stringToDirection(String string) {
        for (Direction dir : Direction.values()){
            if (dir.toString().equals(string)){
                return dir;
            }
        }
        return null;
    }
}
