package ca.mcmaster.se2aa4.island.teamXXX.Commands;

import org.json.JSONObject;

public class StopCommand implements Command {
    // This action doesn't actually use the parameters
    // TODO liskov?
    @Override
    public JSONObject execute(JSONObject parameters) {
        JSONObject decision = new JSONObject();
        decision.put("action", "stop"); // we stop the exploration immediately
        return decision;
    }
}
