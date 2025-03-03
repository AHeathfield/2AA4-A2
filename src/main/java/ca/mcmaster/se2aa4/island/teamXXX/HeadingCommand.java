package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class HeadingCommand implements Command {
    @Override
    public JSONObject execute(JSONObject parameters) {
        JSONObject decision = new JSONObject();
        decision.put("action", "heading");
        decision.put("parameters", parameters);
        return decision;
    }
}
