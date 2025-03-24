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
        CHECK_MORE_LAND,
        RETURN_TO_LAND,
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
        logger.info("current state: {}", lastState);
        logger.info("current dir: {}", computer.getDroneDirection());

        switch (lastState) {
            case ECHO_FORWARD:
                if (distanceAhead != 0) {
                    // fly ahead until at land again
                    // last instruction was fly
                    distanceAhead--;
                    if (distanceAhead == 0) {
                        logger.info("Island found, start scanning");
                        computer.setCurrentState(new CoastSearchState(computer));
                        return new Instruction(Action.SCAN, param);
                    }

                    logger.info("Land ahead, keep flying. It is {}m away", distanceAhead);
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
                    distanceAhead = range+1;
                    if (distanceAhead == 0) {
                        logger.info("back on land found, start scanning");
                        computer.setCurrentState(new CoastSearchState(computer));
                        return new Instruction(Action.SCAN, param);
                    }
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
                    computer.setDroneDirection(Direction.EAST);
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
                    computer.setDroneDirection(Direction.EAST);
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
                // just turned, turn again OR check if more land
                Direction turnDirection = columnDir == Direction.SOUTH ? Direction.NORTH : Direction.SOUTH;
                logger.info("column dir {}", columnDir);
                if (columnDir == computer.getDroneDirection().getRightDirection().getRightDirection()) {
                    // already turned, 
                    lastState = miniState.CHECK_MORE_LAND;
                    param.put("direction", computer.getDroneDirection().toString());
                    return new Instruction(Action.ECHO, param);
                }
                computer.setDroneDirection(turnDirection);
                param.put("direction", turnDirection.toString());
                return new Instruction(Action.HEADING, param);
            case CHECK_MORE_LAND:
                // just echoed forward
                // if land ahead, keep flying (ECHO_FORWARD)
                // else no land ahead, stop
                logger.info("checking for more land ahead");
                boolean noLandAhoy = droneResponse.getJSONObject("extras").get("found").equals("OUT_OF_RANGE");
                if (noLandAhoy) {
                    // no land ahead, stop
                    logger.info("No more land ahead, stop");
                    return new Instruction(Action.STOP, param);
                } else {
                    // land ahead, keep flying
                    lastState = miniState.ECHO_FORWARD;
                    distanceAhead = droneResponse.getJSONObject("extras").getInt("range") + 1;
                    logger.info("Land ahead, keep flying. It is {}m away", distanceAhead);
                    if (distanceAhead == 0) {
                        logger.info("back on land found, start scanning");
                        computer.setCurrentState(new CoastScanState(computer));
                        return new Instruction(Action.FLY, param);
                    }

                    return new Instruction(Action.FLY, param);
                }
            case RETURN_TO_LAND:
                // just flew forward, keep flying until at land
                distanceAhead--;
                if (distanceAhead == 0) {
                    logger.info("Island found, start scanning");
                    computer.setCurrentState(new CoastScanState(computer));
                    return new Instruction(Action.FLY, param);
                }
                logger.info("continue flying, land is {}m away", distanceAhead);
                return new Instruction(Action.FLY, param);
            default:
                logger.error("Invalid state");
                return new Instruction(Action.STOP, param);
        }
    }
}
