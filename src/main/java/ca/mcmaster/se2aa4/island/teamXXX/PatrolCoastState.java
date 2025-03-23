package ca.mcmaster.se2aa4.island.teamXXX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class PatrolCoastState extends State {
    private final Logger logger = LogManager.getLogger();

    // Constructor
    public PatrolCoastState(RescueComputer computer) {
        super(computer);
    }

    // For now these will just be the SCAN action
    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        logger.info("COASTLINE PATROL, current drone direction  = {}", computer.getDroneDirection());
        computer.setCurrentState(new ReturnState(computer));
        return new Instruction(Action.SCAN);
    }
}
