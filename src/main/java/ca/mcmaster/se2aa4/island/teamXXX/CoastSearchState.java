package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CoastSearchState extends State {
    private final Logger logger = LogManager.getLogger();
    private Direction lastEchoDir = null;
    private boolean overOcean = false;
    private boolean overCreek = false;

    // Constructor
    public CoastSearchState(RescueComputer computer) {
        super(computer);
    }

    @Override
    public Instruction determineNextInstruction(JSONObject droneResponse) {
        JSONObject param = new JSONObject();
        Direction droneDir = computer.getDroneDirection(); 

        // proceeding through column
        logger.info("scannned, still searching for creek");
        JSONArray creeksJSON = droneResponse.getJSONObject("extras").getJSONArray("creeks");
        overCreek = creeksJSON.length() > 0;

        // stop if over creek
        if (overCreek) {
            logger.info("Over creek");
            return new Instruction(Action.STOP, param);
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
            param.put("direction", droneDir);
            return new Instruction(Action.ECHO, param);
        } else {
            logger.info("Not over ocean, continue flying");
            computer.setCurrentState(new ReturnState(computer));
            return new Instruction(Action.FLY, param);
        }

    }
}
