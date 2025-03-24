package ca.mcmaster.se2aa4.island.teamXXX.States;
import static ca.mcmaster.se2aa4.island.teamXXX.LoggerUtil.logger;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

// Patrol coast without grid search
public class PatrolCoastState extends State {

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
