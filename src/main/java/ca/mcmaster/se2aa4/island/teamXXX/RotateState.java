package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

// This is too help traverse coast line, it does not consider edge cases
public class RotateState extends State {
    private final Logger logger = LogManager.getLogger();
    private List<Instruction> sequenceOfInstructs;
    private int instructCount;  // To keep track of what instruct we are on
    private Direction droneDir;

    // Constructor
    public RotateState(RescueComputer computer, Turn turn) {
        super(computer);
        this.instructCount = 0;
        this.droneDir = computer.getDroneDirection();
        setupInstructions(turn);
    }

    // Both fill in list of instructs
    private void setupInstructions(Turn turn) {
        this.sequenceOfInstructs = new ArrayList<>();

        // Setting up sequence of instructs 
        // Left: FLY, 3 HEAD.rights, 2 FLY
        // Right: FLY, 3 HEAD.lefts, 2 FLY
        sequenceOfInstructs.add(new Instruction(Action.FLY));

        // Need to add this 3 times
        for (int i = 0; i < 3; i++) {
            JSONObject param = new JSONObject();
            logger.info("Current Drone Dir: {}", droneDir);

            if (turn == Turn.LEFT) {
                droneDir = droneDir.getLeftDirection(); 
            } 
            else if (turn == Turn.RIGHT) {
                droneDir = droneDir.getRightDirection();
            }

            param.put("direction", droneDir.toString()); //already turned, so just toString()
            
            sequenceOfInstructs.add(new Instruction(Action.HEADING, param));
        }

        sequenceOfInstructs.add(new Instruction(Action.FLY));
        sequenceOfInstructs.add(new Instruction(Action.FLY));
    }

    // Mimicking rotate player to right or left directions and move forward 1
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        
        String rotation;
        if (computer.getFormerDroneDirection().getRightDirection() == computer.getDroneDirection()) {
            rotation = "Right";
        } else {
            rotation = "Left";
        }

        logger.info("Rotating {}.... Current count = {}", rotation, instructCount);
        Instruction nextInstruct = sequenceOfInstructs.get(instructCount);
        instructCount++;
        
        // If last instruction
        if (instructCount == sequenceOfInstructs.size()) {
            logger.info("{} rotation COMPLETE!, final direction {}", rotation, droneDir);
            computer.setDroneDirection(droneDir);
            computer.setCurrentState(new PatrolCoastState(computer));
        }

        // SOMEWHERE HERE prob at end just set the computer drone dir to right
        return nextInstruct;
    }
}
