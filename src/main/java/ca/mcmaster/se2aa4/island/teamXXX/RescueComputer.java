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
    private String droneStatus;
    private int counter;

    public RescueComputer(JSONObject info) {
        this.droneDir = stringToDirection(info.getString("heading"));
        this.droneBat = info.getInt("budget");
        logger.info("The drone is facing {}", droneDir.toString());
        logger.info("Battery level is {}", droneBat);
        this.counter = 0;
    }

    @Override
    public void processData(JSONObject droneResponse) {
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
    public Instruction determineNextInstruction() {
        JSONObject params = new JSONObject();
        switch (counter) {
            case 0:
                params.put("direction", droneDir.getLeftDirection().toString());
                return new Instruction(Action.ECHO, params);
            case 1:
                params.put("direction", droneDir.toString());
                return new Instruction(Action.ECHO, params);
            case 2:
                params.put("direction", droneDir.getRightDirection().toString());
                return new Instruction(Action.ECHO, params);
        }
        
        counter++;
        counter = counter % 3;
        return new Instruction(Action.STOP, new JSONObject());
    } 

    //refactor this
    private Direction stringToDirection(String string) {
        switch (string) {
            case "North":
                return Direction.NORTH;
            case "East":
                return Direction.EAST;
            case "South":
                return Direction.SOUTH;
            case "West":
                return Direction.WEST;
        }
        return Direction.EAST;
    }
}
