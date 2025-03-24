package ca.mcmaster.se2aa4.island.teamXXX.States;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

// Patrol coast without grid search
public class PatrolCoastState extends State {
    private Direction lastEchoDir;
    private final Logger logger = LogManager.getLogger();
    private boolean firstInstruction;

    // Constructor
    public PatrolCoastState(RescueComputer computer) {
        super(computer);
        this.firstInstruction = true;
    }

    // NOTE: If rotating, you have to return FLY or rotate will not work
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        // Echo's right, forward, left, if something found next to it move there else
        Direction droneDir = computer.getDroneDirection();
        JSONObject param = new JSONObject();

        // first instruction always scans
        if (firstInstruction) {
            firstInstruction = false;
            return new Instruction(Action.SCAN);
        }
        else if (lastEchoDir == null) {
            param.put("direction", droneDir.getRightDirection().toString());
            lastEchoDir = droneDir.getRightDirection();
            return new Instruction(Action.ECHO, param);
        }


        // This is where we check if anything is found
        boolean landNotFound = droneResponse.getJSONObject("extras").get("found").equals("OUT_OF_RANGE");
        int range = droneResponse.getJSONObject("extras").getInt("range");
        if (landNotFound || (!landNotFound && range != 0)) {
            if (lastEchoDir == droneDir.getLeftDirection()) {
                computer.setCurrentState(new RotateState(computer, Turn.BACKWARDS, droneDir));
                return new Instruction(Action.FLY);
            }
            // if there's no land near you Fly backwards
            else {
                param.put("direction", lastEchoDir.getLeftDirection().toString());
                lastEchoDir = lastEchoDir.getLeftDirection();
                return new Instruction(Action.ECHO, param);
            }
        }
        else {
            // Fly forward
            if (lastEchoDir == droneDir) {
                lastEchoDir = null;
                firstInstruction = true;  // Just restarting the algorithm
                return new Instruction(Action.FLY);
            }
            // Fly left
            else if (lastEchoDir == droneDir.getLeftDirection()) {
                computer.setCurrentState(new RotateState(computer, Turn.LEFT, droneDir));
                return new Instruction(Action.FLY);
            }
            // Fly right
            else {
                computer.setCurrentState(new RotateState(computer, Turn.RIGHT, droneDir));
                return new Instruction(Action.FLY);
            }
        }
    }
}

