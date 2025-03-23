package ca.mcmaster.se2aa4.island.teamXXX.Commands;

import org.json.JSONObject;

public class EchoCommand implements Command {
    @Override
    public JSONObject execute(JSONObject parameters) {
        JSONObject decision = new JSONObject();
        decision.put("action", "echo");
        decision.put("parameters", parameters);
        return decision;
    }
    
}
