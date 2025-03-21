package ca.mcmaster.se2aa4.island.teamXXX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class MoveState extends State {
    private final Logger logger = LogManager.getLogger();

    // Constructor
    public MoveState(RescueComputer computer) {
        super(computer);
    }

    // For now these will just be the STOP action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        decrementDistanceToIsland();    // Remember it enters this state after it's already started moving!
        int islandDistance = computer.getDistanceToIsland();

        // If we are not at island yet
        if (islandDistance > 0) {
            logger.info("Island is currently {}m away.", islandDistance);
            return new Instruction(Action.FLY);
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
