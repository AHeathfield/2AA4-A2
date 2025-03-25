package ca.mcmaster.se2aa4.island.teamXXX.States;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

public class NavigateRotateState extends State {
    private final Logger logger = LogManager.getLogger();
    private List<Instruction> sequenceOfInstructs;
    private int instructCount;  // To keep track of what instruct we are on
    private Direction droneDir;
    public NavigateRotateState(RescueComputer computer, Turn turn) {
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
        // Backwards: FLY, 3 HEAD.rights, HEAD.left
        
        // first fly done before this state (in NavigateToCreekState)
        //sequenceOfInstructs.add(new Instruction(Action.FLY));

        for (int i = 0; i < 3; i++) {
            JSONObject param = new JSONObject();

            if (turn == Turn.RIGHT) {
                droneDir = droneDir.getLeftDirection(); 
            } else {
                droneDir = droneDir.getRightDirection();
            }

            param.put("direction", droneDir.toString()); //already turned, so just toString()
            
            sequenceOfInstructs.add(new Instruction(Action.HEADING, param));
        }

        if (turn == Turn.BACKWARDS) {
            JSONObject param = new JSONObject();
            droneDir = droneDir.getLeftDirection();
            param.put("direction", droneDir.toString());
            sequenceOfInstructs.add(new Instruction(Action.HEADING, param));
        } else {
            sequenceOfInstructs.add(new Instruction(Action.FLY));
            sequenceOfInstructs.add(new Instruction(Action.FLY));
        }
    }

    // Mimicking rotate player to right or left directions and move forward 1
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {

        Instruction nextInstruct = sequenceOfInstructs.get(instructCount);
        instructCount++;

        if (nextInstruct.getAction() == Action.HEADING) {
            Direction newDir = computer.stringToDirection(nextInstruct.getParameters().getString("direction"));
            computer.setDroneDirection(newDir);
        }
        
        // If last instruction
        if (instructCount == sequenceOfInstructs.size()) {
            logger.info("Finished rotating");
            computer.setCurrentState(new NavigateToCreekState(computer));
        }

        return nextInstruct;
    }
    
}
