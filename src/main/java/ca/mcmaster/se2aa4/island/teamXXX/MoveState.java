package ca.mcmaster.se2aa4.island.teamXXX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class MoveState extends State {
    private final Logger logger = LogManager.getLogger();
    private boolean testing = true;     // Set this to true if you want to do test state
    // private Instruction instructFromLastState;  // The instruction that lead into this state

    // Constructor
    public MoveState(RescueComputer computer) {
        super(computer);
        // instructFromLastState = computer.getLastInstruction();
    }

    // For now these will just be the STOP action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        // THIS IS FOR TESTING JUST SET VAR TO FALSE IF NOT
        if (testing) {
            computer.setCurrentState(new TestMoveState(computer));
            return new Instruction(Action.SCAN);
        }




        decrementDistanceToIsland();    // Remember it enters this state after it's already started moving!
        int islandDistance = computer.getDistanceToIsland();
        Direction currentDir = computer.getDroneDirection();
        // TODO: Need to handle the special case where the island is right beside it
        // ANSWER: treat it like a coast search??
        

        // If we are not at island yet
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
                if (computer.getFormerDroneDirection().equals(currentDir.getRightDirection())) {
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
            computer.setCurrentState(new ReturnState(computer));
            return new Instruction(Action.SCAN);
        }
    }

    private void decrementDistanceToIsland() {
        int decremented = computer.getDistanceToIsland() - 1;
        computer.setDistanceToIsland(decremented);
    }
}
