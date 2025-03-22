package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CheckForTurnState extends State {
    private final Logger logger = LogManager.getLogger();

    enum miniState {
        ECHO_FORWARD,
        ECHO_SIDE,
        FLY_ALONG_SIDE,
        TURN,

    }
    miniState lastState  = miniState.ECHO_FORWARD;
    int distanceAhead = 0;
    Direction columnDir = Direction.SOUTH;

    // Constructor
    public CheckForTurnState(RescueComputer computer) {
        super(computer);
    }

    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        // just echoed forward
        // if land ahead, keep flying
        //    if distance becomes 0, scan, coast search state
        // else no land ahead, echo to side
        //     if land to side, keep flying
        //     else no land to side, start turn
        
        JSONObject param = new JSONObject();

        switch (lastState) {
            case ECHO_FORWARD:
                if (distanceAhead != 0) {
                    // fly ahead until at land again
                    // last instruction was fly
                    distanceAhead--;
                    if (distanceAhead == 0) {
                        logger.info("Island found, start checking side");
                        computer.setCurrentState(new CoastSearchState(computer));
                        return new Instruction(Action.SCAN, param);
                    }

                    logger.info("Land ahead, keep flying");
                    return new Instruction(Action.FLY, param);
                }

                // last instruction was echo forward
                boolean landNotFound = droneResponse.getJSONObject("extras").get("found").equals("OUT_OF_RANGE");

                if (landNotFound) {
                    // start scanning side
                    distanceAhead = 0;
                    logger.info("No Island found, start checking side");
                    lastState = miniState.ECHO_SIDE;
                    Direction echoDir = Direction.EAST;
                    param.put("direction", echoDir.toString());
                    return new Instruction(Action.ECHO, param);
                } else {
                    int range = droneResponse.getJSONObject("extras").getInt("range");
                    distanceAhead = range;
                    logger.info("more land ahead! It is {}m away", range);
                    return new Instruction(Action.FLY, param);
                }
            case ECHO_SIDE:
                boolean landNotFound1 = droneResponse.getJSONObject("extras").get("found").equals("OUT_OF_RANGE");

                // no land to side, start turning
                if (landNotFound1) {
                    // start turning
                    logger.info("No land to side, start turning");
                    lastState = miniState.TURN;
                    columnDir = computer.getDroneDirection();
                    param.put("direction", Direction.EAST.toString());
                    return new Instruction(Action.HEADING, param);
                }

                int range = droneResponse.getJSONObject("extras").getInt("range");
                if (range < 2) {
                    // land to side, keep flying
                    logger.info("Land to side, keep flying");
                    lastState = miniState.FLY_ALONG_SIDE;
                    return new Instruction(Action.FLY, param);
                } else {
                    // no land directly to side, start turning
                    logger.info("start turning");
                    lastState = miniState.TURN;
                    columnDir = computer.getDroneDirection();
                    param.put("direction", Direction.EAST.toString());
                    return new Instruction(Action.HEADING, param);
                }
            case FLY_ALONG_SIDE:
                // last instruction was fly
                // echo side again
                lastState = miniState.ECHO_SIDE;
                param.put("direction", Direction.EAST.toString());
                return new Instruction(Action.ECHO, param);
            case TURN:
                // just turned, turn again OR 
                Direction turnDirection = columnDir == Direction.SOUTH ? Direction.NORTH : Direction.SOUTH;
                if (columnDir == computer.getDroneDirection()) {
                    // already turned, 
                }
                param.put("direction", turnDirection.toString());
                return new Instruction(Action.HEADING, param);
                
        }

        computer.setCurrentState(new CoastSearchState(computer));
        return new Instruction(Action.SCAN, param);
    }
}
