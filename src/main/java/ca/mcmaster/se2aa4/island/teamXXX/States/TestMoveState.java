package ca.mcmaster.se2aa4.island.teamXXX.States;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ca.mcmaster.se2aa4.island.teamXXX.*;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import org.json.JSONObject;

// scans every time to see the path it took
public class TestMoveState extends State {
    // private Instruction instructFromLastState;  // The instruction that lead into this state
    private final Logger logger = LogManager.getLogger();
    private boolean lastActionWasTestSCAN = false;
    private boolean isFirstRun = true;
    private Direction formerDroneDir;   // Useful for when it turns to know what dir it was before
    private int islandDistance;

    // Constructor
    public TestMoveState(RescueComputer computer, Direction formerDroneDir, int islandDistance) {
        super(computer);
        this.formerDroneDir = formerDroneDir;
        this.islandDistance = islandDistance + 1;   // for testing...
    }


    // For now these will just be the STOP action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        // To account for move going into state
        if (isFirstRun) {
            decrementDistanceToIsland();
            isFirstRun = false;
        }

        Direction currentDir = computer.getDroneDirection();
        Position currentPos = computer.getDronePosition();
        
        if (lastActionWasTestSCAN) {
            decrementDistanceToIsland();    // Remember it enters this state after it's already started moving!
            lastActionWasTestSCAN = false;
            // If we are not at island yet
            if (islandDistance > 1) {
                logger.info("Island is currently {}m away.", islandDistance);
                return new Instruction(Action.FLY);
            }
            // Handling case for if we turned or not 
            else if (islandDistance == 1) {
                logger.info("Island is currently {}m away.", islandDistance);
                logger.info("Former: {}, Current: {}", formerDroneDir, currentDir);
                
                // Need to see if we turned before heading to island
                // If we did not turn
                if (currentDir == formerDroneDir) {
                    return new Instruction(Action.FLY);
                }
                // If we did turn
                else {
                    JSONObject param = new JSONObject();

                    // Now we need to see if we turned left or right
                    // if it turned right
                    if (formerDroneDir.equals(currentDir.getRightDirection())) {
                        param.put("direction", currentDir.getLeftDirection().toString());
                        computer.setDroneDirection(currentDir.getLeftDirection());
                    }
                    else {
                        param.put("direction", currentDir.getRightDirection().toString());
                        computer.setDroneDirection(currentDir.getRightDirection());
                    }

                    return new Instruction(Action.HEADING, param);
                }
            }
            else {
                logger.info("Island has been reached!");
                computer.setCurrentState(new RotateState(computer, Turn.LEFT, formerDroneDir)); // TEST: rotate right
                return new Instruction(Action.SCAN);
            }
        }
        
        // scanning for testing
        else {
            lastActionWasTestSCAN = true;
            return new Instruction(Action.SCAN);
        }
    }

    private void decrementDistanceToIsland() {
        islandDistance--;
    }
}
