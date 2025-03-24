package ca.mcmaster.se2aa4.island.teamXXX.States;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;
import org.json.JSONObject;

public class MoveState extends State {
    private final Logger logger = LogManager.getLogger();
    private Direction formerDroneDir;   // Useful for when it turns to know what dir it was before
    private int islandDistance;

    private boolean gridSearch = true; // Set this to true if you want to do grid search
    private boolean testing = false;     // Set this to true if you want to do test state

    // Constructor
    public MoveState(RescueComputer computer, Direction formerDroneDir, int islandDistance) {
        super(computer);
        this.formerDroneDir = formerDroneDir;
        this.islandDistance = islandDistance;
    }

    // For now these will just be the STOP action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        // THIS IS FOR TESTING JUST SET VAR TO FALSE IF NOT
        if (testing) {
            computer.setCurrentState(new TestMoveState(computer, formerDroneDir, islandDistance));
            return new Instruction(Action.SCAN);
        }

        decrementDistanceToIsland();    // Remember it enters this state after it's already started moving!
        Direction currentDir = computer.getDroneDirection();
        Position currentPos = computer.getDronePosition();
        // TODO: Need to handle the special case where the island is right beside it
        // ANSWER: treat it like a coast search??
        
        if (gridSearch) return gridSearch_NextInstruction(currentDir, currentPos);

        // If we are not at island yet
        if (islandDistance > 1) {
            logger.info("Island is currently {}m away.", islandDistance);
            computer.setDronePosition(currentPos.getForwardPosition(currentDir));
            return new Instruction(Action.FLY);
        }
        // Handling case for if we turned or not 
        else if (islandDistance == 1) {
            logger.info("Island is currently {}m away.", islandDistance);
            logger.info("Former: {}, Current: {}", formerDroneDir, currentDir);
            
            // Need to see if we turned before heading to island
            // If we did not turn
            if (currentDir == formerDroneDir) {
                computer.setDronePosition(currentPos.getForwardPosition(currentDir));
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
                    computer.setDronePosition(currentPos.getLeftPosition(currentDir));
                }
                else {
                    param.put("direction", currentDir.getRightDirection().toString());
                    computer.setDroneDirection(currentDir.getRightDirection());
                    computer.setDronePosition(currentPos.getRightPosition(currentDir));
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
        islandDistance--;
    }

    private Instruction gridSearch_NextInstruction(Direction currentDir, Position currentPos) {
        if (islandDistance > 0) {
            logger.info("Island is currently {}m away.", islandDistance);
            computer.setDronePosition(currentPos.getForwardPosition(currentDir));
            return new Instruction(Action.FLY);
        }
        else {
            logger.info("Island has been reached!");
            computer.setCurrentState(new CoastSearchState(computer));
            return new Instruction(Action.SCAN);
        }
    }
}
