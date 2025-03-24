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

    // Constructor
    public PatrolCoastState(RescueComputer computer) {
        super(computer);
    }

    // For now these will just be the SCAN action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        // Echo's right, forward, left, if something found next to it move there else
        Direction droneDir = computer.getDroneDirection();
        JSONObject param = new JSONObject();

        if (lastEchoDir == null) {
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
                return new Instruction(Action.SCAN);
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
                return new Instruction(Action.FLY);
            }
            // Fly left
            else if (lastEchoDir == droneDir.getLeftDirection()) {
                computer.setCurrentState(new RotateState(computer, Turn.LEFT, droneDir));
                return new Instruction(Action.SCAN);
            }
            // Fly right
            else {
                computer.setCurrentState(new RotateState(computer, Turn.RIGHT, droneDir));
                return new Instruction(Action.SCAN);
            }
        }
    }
}

