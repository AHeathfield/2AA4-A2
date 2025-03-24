package ca.mcmaster.se2aa4.island.teamXXX.States;

import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ca.mcmaster.se2aa4.island.teamXXX.RescueComputer;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

public class NavigateToCreekState extends State {
    private final Logger logger = LogManager.getLogger();

    public NavigateToCreekState(RescueComputer computer, int creekX, int creekY) {
        super(computer);
    }

    public Instruction determineNextInstruction(JSONObject droneResponse) {
        JSONObject param = new JSONObject();
        Position dronePos = computer.getDronePosition();
        Position creekPos = computer.getNearestCreekPosition();
        Direction droneDir = computer.getDroneDirection();

        int deltaX = creekPos.x - dronePos.x;
        int deltaY = creekPos.y - dronePos.y;

        if (deltaX == 0 && deltaY == 0) {
            logger.info("Arrived at creek! landing!");
            return new Instruction(Action.LAND);
        }

        // handle facing the right y direction
        if (deltaY > 0 && droneDir == Direction.NORTH || deltaY < 0 && droneDir == Direction.SOUTH) {
            // u turn!
            
        }

        // handle case of starting with delta y = 0
        if (deltaY == 0) {
            //  hard turn
        }

        // if starting at correct x value
        if (deltaX == 0) {
            logger.info("Drone at creek's x position");

            // drone is heading towards creek
            if (deltaY > 0 && droneDir == Direction.SOUTH) {
                return new Instruction(Action.FLY, param);
            } else if (deltaY < 0 && droneDir == Direction.NORTH) {
                return new Instruction(Action.FLY, param);
            }
        }

        if (droneDir == Direction.NORTH || droneDir == Direction.SOUTH) {
            return handleYMovement(deltaX, deltaY, droneDir, dronePos, creekPos);
        } 

        if (droneDir == Direction.EAST || droneDir == Direction.WEST) {
            return new Instruction(Action.FLY, param);
        }

        logger.info("you did not account for this state!!!");
        return new Instruction(Action.STOP);
    }

    private Instruction handleYMovement(int deltaX, int deltaY, Direction droneDir, Position dronePos, Position creekPos) {
        JSONObject param = new JSONObject();
        if (deltaY == 1) {
            // turn in drone dir
            if (deltaX > 0) {
                param.put("direction", Direction.EAST.toString());
            } else {
                param.put("direction", Direction.WEST.toString());
            }
            return new Instruction(Action.HEADING, param);
        }
        
        // deltaY > 1
        return new Instruction(Action.FLY, param);
    }
}
