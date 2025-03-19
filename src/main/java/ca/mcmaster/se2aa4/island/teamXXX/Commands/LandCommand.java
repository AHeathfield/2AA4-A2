package ca.mcmaster.se2aa4.island.teamXXX.Commands;

import org.json.JSONObject;

public class LandCommand implements Command {
    public JSONObject execute(JSONObject parameters) {
        JSONObject decision = new JSONObject();
        decision.put("action", "land");
        decision.put("parameters", parameters);
        return decision;
    }
}
