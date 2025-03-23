package ca.mcmaster.se2aa4.island.teamXXX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

// THIS JUST SCANS EVERY TIME SO I CAN SEE THE PATH IT TOOK!!!
public class TestMoveState extends State {
    private final Logger logger = LogManager.getLogger();
    // private Instruction instructFromLastState;  // The instruction that lead into this state
    private boolean lastActionWasTestSCAN = false;
    private boolean isFirstRun = true;

    // Constructor
    public TestMoveState(RescueComputer computer) {
        super(computer);
        // instructFromLastState = computer.getLastInstruction();
    }

    // For now these will just be the STOP action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        // To account for move going into state
        if (isFirstRun) {
            decrementDistanceToIsland();
            isFirstRun = false;
        }

        int islandDistance = computer.getDistanceToIsland();
        Direction currentDir = computer.getDroneDirection();
        // TODO: Need to handle the special case where the island is right beside it
        // ANSWER: treat it like a coast search??
        
        if (lastActionWasTestSCAN) {
            decrementDistanceToIsland();    // Remember it enters this state after it's already started moving!
            lastActionWasTestSCAN = false;
            // If we are not at island yetyet
            if (islandDistance > 1) {
                logger.info("Island is currently {}m away.", islandDistance);
                return new Instruction(Action.FLY);
            }
            // Handling case for if we turned or not 
            else if (islandDistance == 1) {
                logger.info("Island is currently {}m away.", islandDistance);
                logger.info("Former: {}, Current: {}", computer.getFormerDroneDirection(), currentDir);
                
                // Need to see if we turned before heading to island
                // If we did not turn
                if (currentDir == computer.getFormerDroneDirection()) {
                    return new Instruction(Action.FLY);
                }
                // If we did turn
                else {
                    JSONObject param = new JSONObject();

                    // Now we need to see if we turned left or right
                    // if it turned right
                    logger.info("Former dir: {}, Current dir: {}", computer.getFormerDroneDirection(), currentDir.getRightDirection());
                    if (computer.getFormerDroneDirection() == currentDir.getRightDirection()) {
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
                computer.setCurrentState(new RotateLeftState(computer)); // TEST: rotate right
                return new Instruction(Action.SCAN);
            }
        }
        
        // SCANNING FOR TESTING!!
        else {
            lastActionWasTestSCAN = true;
            return new Instruction(Action.SCAN);
        }
    }

    private void decrementDistanceToIsland() {
        int decremented = computer.getDistanceToIsland() - 1;
        computer.setDistanceToIsland(decremented);
    }
}
