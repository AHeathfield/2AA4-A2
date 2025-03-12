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

    public RescueComputer() {
        // TODO assign members
        this.droneBat = 1000; //TEMP
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
        // TODO add logic
        return new Instruction(Action.STOP, new JSONObject());
    } 
}
