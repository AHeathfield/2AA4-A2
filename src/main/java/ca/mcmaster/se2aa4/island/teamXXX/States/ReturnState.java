package ca.mcmaster.se2aa4.island.teamXXX;
import static ca.mcmaster.se2aa4.island.teamXXX.LoggerUtil.logger;
import org.json.JSONObject;

public class ReturnState extends State {

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
