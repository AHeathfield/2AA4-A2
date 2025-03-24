package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.List;
import java.util.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

// This will be the "brain", it will take current data and determine next moves
public class RescueComputer implements Computer {
    private final Logger logger = LogManager.getLogger();
    private List<Instruction> instructHistory;  // Will be good for calculating final distance
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

        this.instructHistory = new Stack<>();
        this.currentState = new SearchState(this);  // First sequence after scan should be a search
        this.nextInstruction = new Instruction(Action.SCAN, new JSONObject());
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
        
        // This lets the state pattern determine the next instruction based on current state
        nextInstruction = currentState.determineNextInstruction(droneResponse);
    }

    @Override
    public Instruction getNextInstruction() {
        // I believe nextInstruction is properly handled by the state pattern
        instructHistory.add(nextInstruction);
        return nextInstruction;
    } 

    // Might put the next 2 methods in interface?
    // Leaky abstractions?? Its ok lol
    // Getters
    public Direction getDroneDirection() { return droneDir; }
    public Instruction getLastInstruction() { return instructHistory.get(instructHistory.size() - 1); }

    // Setters
    public void setCurrentState(State state) {
        currentState = state;
    }
    public void setDroneDirection(Direction dir) {
        droneDir = dir;
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
