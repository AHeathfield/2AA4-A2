package ca.mcmaster.se2aa4.island.teamXXX.States;

import static ca.mcmaster.se2aa4.island.teamXXX.LoggerUtil.logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.States.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

public class CoastSearchState extends State {
    private Direction lastEchoDir = null;
    private boolean overOcean = false;
    private boolean overCreek = false;
    private boolean siteFound = false;

    // Constructor
    public CoastSearchState(RescueComputer computer) {
        super(computer);
    }

    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        JSONObject param = new JSONObject();
        Direction droneDir = computer.getDroneDirection(); 
        Position dronePos = computer.getDronePosition();

        // proceeding through column
        logger.info("scannned, still searching for creek");
        JSONArray creeksJSON = droneResponse.getJSONObject("extras").getJSONArray("creeks");
        overCreek = creeksJSON.length() > 0;

        // check if over site
        JSONArray sitesJSON = droneResponse.getJSONObject("extras").getJSONArray("sites");
        siteFound = sitesJSON.length() > 0;
        if (siteFound) {
            logger.info("------------- Site found ----------------");
            computer.setEmergencySite(dronePos);
            computer.displayIslandMap();
            computer.displayCreeks();
            return new Instruction(Action.STOP, param);
        }

        // check if over creek
        if (overCreek) {
            logger.info("------------- Over creek ----------------");
            computer.addCreekFound(dronePos);
            //return new Instruction(Action.STOP, param);
        }

        JSONArray biomesJSON = droneResponse.getJSONObject("extras").getJSONArray("biomes");
        List<String> biomes = new ArrayList<String>(biomesJSON.length());
        for (int i = 0; i < biomesJSON.length(); i++) {
            biomes.add(biomesJSON.getString(i));
        }
        overOcean = (biomes.size() == 1 && biomes.contains("OCEAN")); // maybe this works

        if (overOcean) {
            logger.info("Over ocean, looking to start turn sequence");
            computer.setCurrentState(new CheckForTurnState(computer));
            param.put("direction", droneDir.toString());
            return new Instruction(Action.ECHO, param);
        } else {
            logger.info("Not over ocean, continue flying");
            computer.setCurrentState(new CoastScanState(computer));
            return new Instruction(Action.FLY, param);
        }

    }
}
