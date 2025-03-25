package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.States.*;

public class Explorer implements IExplorerRaid {
    private final Logger logger = LogManager.getLogger();
    private Computer computer; //RescueComputer
    private Controller controller = new DroneController();

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        computer = new RescueComputer(info);
    }

    int count = 0;
    @Override
    public String takeDecision() {
        Instruction instruction = computer.getNextInstruction();
        JSONObject decision = controller.pressButton(instruction.getAction(), instruction.getParameters());
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        computer.processData(response);
    }

    @Override
    public String deliverFinalReport() {
        RescueComputer temp = (RescueComputer) computer;
        logger.info("FINALY BATTERY: {}", temp.getDroneBattery());
        return "no creek found";
    }

}
