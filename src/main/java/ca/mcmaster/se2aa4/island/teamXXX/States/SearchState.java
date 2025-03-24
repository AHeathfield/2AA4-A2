package ca.mcmaster.se2aa4.island.teamXXX;
import static ca.mcmaster.se2aa4.island.teamXXX.LoggerUtil.logger;
import org.json.JSONObject;

public class SearchState extends State {
    private Direction lastEchoDir;

    // Constructor
    public SearchState(RescueComputer computer) {
        super(computer);
    }

    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        Direction droneDir = computer.getDroneDirection(); 
        Position dronePos = computer.getDronePosition();
        JSONObject param = new JSONObject();    // Stores the params for the next Instruction

        // First instruction case (echo forward)
        if (lastEchoDir == null) {
            logger.info("Starting Search");
            param.put("direction", droneDir.toString());
            lastEchoDir = droneDir;
            return new Instruction(Action.ECHO, param);
        }
        
        // Getting range from last echo
        boolean islandNotFound = droneResponse.getJSONObject("extras").get("found").equals("OUT_OF_RANGE");
        int range = droneResponse.getJSONObject("extras").getInt("range");
        
        // if no island in range no island is found
        if (islandNotFound) {
            logger.info("No Island found, continuing scan");
            return nextSearchInstruction(droneDir, dronePos);
        }
        // If there is an island that has been scanned
        else {
            logger.info("Island has been found! It is {}m away", range);
            return nextMoveInstruction(droneDir, dronePos, range + 1);
        }
    }


    // These are just helpers to help organize code
    private Instruction nextSearchInstruction(Direction droneDir, Position dronePos) {
        JSONObject param = new JSONObject();
        if (lastEchoDir == droneDir) {
            logger.info("echoing right");
            Direction right = droneDir.getRightDirection();
            param.put("direction", right.toString());
            lastEchoDir = right;
            return new Instruction(Action.ECHO, param);
        }
        else if (lastEchoDir == droneDir.getRightDirection()) {
            logger.info("echoing left");
            Direction left = droneDir.getLeftDirection();
            param.put("direction", left.toString());
            lastEchoDir = left;
            return new Instruction(Action.ECHO, param);
        }
        // Checked all directions, move forward
        else {
            // I don't think we should change the state to MoveState because it's just moving forward to continue search
            lastEchoDir = null;     // Null to essentially restart the search sequence
            computer.setDronePosition(dronePos.getForwardPosition(droneDir));
            return new Instruction(Action.FLY);
        }
    }

    private Instruction nextMoveInstruction(Direction droneDir, Position dronePos, int range) {
        JSONObject param = new JSONObject();

        if (lastEchoDir == droneDir) {
            computer.setCurrentState(new MoveState(computer, computer.getDroneDirection(), range));
            computer.setDronePosition(dronePos.getForwardPosition(droneDir));
            return new Instruction(Action.FLY);
        }
        else if (lastEchoDir == droneDir.getRightDirection()) {
            computer.setCurrentState(new MoveState(computer, computer.getDroneDirection(), range));
            param.put("direction", droneDir.getRightDirection().toString());
            computer.setDroneDirection(droneDir.getRightDirection());
            computer.setDronePosition(dronePos.getRightPosition(droneDir));
            return new Instruction(Action.HEADING, param);
        }
        else {
            computer.setCurrentState(new MoveState(computer, computer.getDroneDirection(), range));
            param.put("direction", droneDir.getLeftDirection().toString());
            computer.setDroneDirection(droneDir.getLeftDirection());
            computer.setDronePosition(dronePos.getLeftPosition(droneDir));
            return new Instruction(Action.HEADING, param);
        }
    }
}
