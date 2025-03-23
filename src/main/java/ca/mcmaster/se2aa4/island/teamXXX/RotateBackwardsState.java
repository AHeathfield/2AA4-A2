package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

// This is too help traverse coast line, it does not consider edge cases
public class RotateBackwardsState extends State {
    private final Logger logger = LogManager.getLogger();
    private List<Instruction> sequenceOfInstructs;
    private int instructCount;  // To keep track of what instruct we are on
    private Direction droneDir;

    // Constructor
    public RotateBackwardsState(RescueComputer computer) {
        super(computer);
        this.instructCount = 0;
        this.droneDir = computer.getDroneDirection();
        setupInstructions();

    }

    // Fills in list of instructs
    private void setupInstructions() {
        this.sequenceOfInstructs = new ArrayList<>();

        // Setting up sequence of instructs FLY, 3 HEAD.rights, HEAD.left
        sequenceOfInstructs.add(new Instruction(Action.FLY));

        // Need to add this 3 times
        for (int i = 0; i < 3; i++) {
            JSONObject param = new JSONObject();
            logger.info("Current Drone Dir: {}", droneDir);
            param.put("direction", droneDir.getRightDirection().toString());
            droneDir = droneDir.getRightDirection();
            sequenceOfInstructs.add(new Instruction(Action.HEADING, param));
        }
        
        JSONObject param = new JSONObject();
        param.put("direction", droneDir.getLeftDirection().toString());
        droneDir = droneDir.getLeftDirection();
        sequenceOfInstructs.add(new Instruction(Action.HEADING, param));
    }

    // Mimicking rotate player to right directions and move forward 1
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        logger.info("Rotating right.... Current count = {}", instructCount);
        Instruction nextInstruct = sequenceOfInstructs.get(instructCount);
        instructCount++;
        
        // If last instruction
        if (instructCount == sequenceOfInstructs.size()) {
            logger.info("Right rotation COMPLETE!, final direction {}", droneDir);
            computer.setDroneDirection(droneDir);
            computer.setCurrentState(new PatrolCoastState(computer));
        }

        // SOMEWHERE HERE prob at end just set the computer drone dir to right
        return nextInstruct;
    }
}
