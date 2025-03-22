package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CoastSearchState extends State {
    private final Logger logger = LogManager.getLogger();
    private Direction lastEchoDir = null;
    private boolean overOcean = false;
    private boolean overCreek = false;
    private boolean checkForTurn = false;
    private int turnCount = -1;

    // Constructor
    public CoastSearchState(RescueComputer computer) {
        super(computer);
    }

    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        JSONObject param = new JSONObject();
        Direction droneDir = computer.getDroneDirection(); 

        if (turnCount < 0) {
            // proceeding through column
            logger.info("scannned, still searching for creek");
            JSONArray creeksJSON = droneResponse.getJSONObject("extras").getJSONArray("creeks");
            overCreek = creeksJSON.length() > 0;

            // stop if over creek
            if (overCreek) {
                logger.info("Over creek");
                return new Instruction(Action.STOP, param);
            }

            JSONArray biomesJSON = droneResponse.getJSONObject("extras").getJSONArray("biomes");
            List<String> biomes = new ArrayList<String>(biomesJSON.length());
            for (int i = 0; i < biomesJSON.length(); i++) {
                biomes.add(biomesJSON.getString(i));
            }
            overOcean = (biomes.size() == 1 && biomes.contains("OCEAN")); // maybe this works

            if (!overOcean) {
                logger.info("Not over ocean, continue flying");
                computer.setCurrentState(new ReturnState(computer));
                return new Instruction(Action.FLY, param);
            }
            if (overOcean) {
                computer.setCurrentState(new CheckForTurnState(computer));
                logger.info("Over ocean, looking to start turn sequence");
                param.put("direction", droneDir);
                return new Instruction(Action.ECHO, param);
            }
            
            
        } else {
            if (turnCount == 0) {
                logger.info("Over ocean, starting turn sequence");
                if (droneDir == Direction.SOUTH) {
                    param.put("direction", droneDir.getLeftDirection().toString());
                    return new Instruction(Action.HEADING, param);
                } else {
                    param.put("direction", droneDir.getRightDirection().toString());
                    return new Instruction(Action.HEADING, param);
                }
            }
        }


        
        // temp
        return new Instruction(Action.STOP, param);

        // continue search

    }

    private Instruction turnSequenceInstruction() {
        return new Instruction(null, null);
    }


    //     if (lastEchoDir == droneDir) {
    //         logger.info("echoing right");
    //         Direction right = droneDir.getRightDirection();
    //         param.put("direction", right.toString());
    //         lastEchoDir = right;
    //         return new Instruction(Action.ECHO, param);
    //     }
    //     else if (lastEchoDir == droneDir.getRightDirection()) {
    //         logger.info("echoing left");
    //         Direction left = droneDir.getLeftDirection();
    //         param.put("direction", left.toString());
    //         lastEchoDir = left;
    //         return new Instruction(Action.ECHO, param);
    //     }
    //     // Checked all directions, move forward
    //     else {
    //         // I don't think we should change the state to MoveState because it's just moving forward to continue search
    //         lastEchoDir = null;     // Null to essentially restart the search sequence
    //         return new Instruction(Action.FLY);
    //     }
    // }

    private Instruction nextMoveInstruction(Direction droneDir) {
        JSONObject param = new JSONObject();

        if (lastEchoDir == droneDir) {
            computer.setCurrentState(new MoveState(computer));
            return new Instruction(Action.FLY);
        }
        else if (lastEchoDir == droneDir.getRightDirection()) {
            computer.setCurrentState(new MoveState(computer));
            param.put("direction", droneDir.getRightDirection().toString());
            return new Instruction(Action.HEADING, param);
        }
        else {
            computer.setCurrentState(new MoveState(computer));
            param.put("direction", droneDir.getLeftDirection().toString());
            return new Instruction(Action.HEADING, param);
        }
    }
}
