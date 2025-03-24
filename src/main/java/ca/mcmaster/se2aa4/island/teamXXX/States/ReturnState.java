package ca.mcmaster.se2aa4.island.teamXXX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

public class ReturnState extends State {
    private final Logger logger = LogManager.getLogger();

    // Constructor
    public ReturnState(RescueComputer computer) {
        super(computer);
    }

    // For now these will just be the STOP action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        logger.info("Returning...");
        computer.displayIslandMap();
        return new Instruction(Action.STOP);
    }
}
