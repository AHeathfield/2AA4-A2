package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {
    private Computer computer = new RescueComputer();
    private Controller controller = new DroneController();
    private final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    int count = 0;
    @Override
    public String takeDecision() {
        // JSONObject decision = new JSONObject();
        // decision.put("action", "stop"); // we stop the exploration immediately
        // return decision.toString();

        // TODO use computer.determineNextAction to send to drone
        // PROBLEM stop doesn't need any parameters so im passing empty JSON
        
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        if (count == 0) {
            // FLY ACTION
            // decision = controller.pressButton(Action.FLY, parameters);

            // SCAN ACTION
            // decision = controller.pressButton(Action.SCAN, parameters);

            // ECHO ACTION
            // parameters.put("direction", "E");
            // decision = controller.pressButton(Action.ECHO, parameters);

            // HEADING ACTION
            parameters.put("direction", "S");
            decision = controller.pressButton(Action.HEADING, parameters);

            // LAND ACTION
            // parameters.put("creek", "id");
            // parameters.put("people", "1");
            // decision = controller.pressButton(Action.LAND, parameters);

            count++;
        } else {
            // STOP ACTION
            decision = controller.pressButton(Action.STOP, parameters);
        }

        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        // logger.info("** Response received:\n"+response.toString(2));
        // Integer cost = response.getInt("cost");
        // logger.info("The cost of the action was {}", cost);
        // String status = response.getString("status");
        // logger.info("The status of the drone is {}", status);
        // JSONObject extraInfo = response.getJSONObject("extras");
        // logger.info("Additional information received: {}", extraInfo);
        
        // Abstraction
        computer.processData(response);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
