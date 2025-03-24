package ca.mcmaster.se2aa4.island.teamXXX.States;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

public class SearchZigzagState extends State {
    private final Logger logger = LogManager.getLogger();
    private Turn formerDroneTurn = null;
    private Action formerDroneAct = Action.ECHO;

    public SearchZigzagState(RescueComputer computer) {
        super(computer);
    }

    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        Direction droneDir = computer.getDroneDirection();
        JSONObject param = new JSONObject();    // Stores the params for the next Instruction
        Instruction instruction = new Instruction(null, null);

        logger.info("Zigzagging");
        // right, echo, analyze, left, repeat until island
        if (formerDroneTurn == Turn.LEFT) {
            param.put("direction", droneDir.getRightDirection().toString());
            formerDroneTurn = Turn.RIGHT;
            computer.setDroneDirection(droneDir.getRightDirection());
            formerDroneAct = Action.HEADING;
            instruction.setAction(Action.HEADING);
        }
        else if (formerDroneTurn == Turn.RIGHT) {
            if (formerDroneAct == Action.HEADING) {
                param.put("direction", droneDir.toString());
                formerDroneAct = Action.ECHO;
                instruction.setAction(Action.ECHO);
            }
            else if (formerDroneAct == Action.ECHO) {
                boolean islandFound = !(droneResponse.getJSONObject("extras").get("found").equals("OUT_OF_RANGE"));
                int range = droneResponse.getJSONObject("extras").getInt("range");
                if (islandFound && range == 0) {
                    logger.info("Island has been found! It is {}m away", range);
                    computer.setCurrentState(new PatrolCoastState(computer));
                    instruction.setAction(Action.FLY);
                }
                else {
                    logger.info("No Island found, continuing scan");
                    param.put("direction", droneDir.getLeftDirection().toString());
                    formerDroneTurn = Turn.LEFT;
                    computer.setDroneDirection(droneDir.getLeftDirection());
                    formerDroneAct = Action.HEADING;
                    instruction.setAction(Action.HEADING);
                }
            }
        }
        else { // first move echo
            param.put("direction", droneDir.toString());
            formerDroneTurn = Turn.LEFT; // not meant to be accurate
            instruction.setAction(Action.ECHO);
        } 
        instruction.setParameters(param);
        return instruction;
    }
}
