package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class FlyCommand implements Command {
    
    @Override
    public JSONObject execute(JSONObject parameters) {
        JSONObject decision = new JSONObject();
        decision.put("action", "fly");
        return decision;
    }
}
