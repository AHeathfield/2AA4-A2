package ca.mcmaster.se2aa4.island.teamXXX.Commands;

import org.json.JSONObject;

public class ScanCommand implements Command {
    @Override
    public JSONObject execute(JSONObject parameters) {
        JSONObject decision = new JSONObject();
        decision.put("action", "scan");
        return decision;
    }
}
