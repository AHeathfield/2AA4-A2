package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Instruction {
    private Action action;
    private JSONObject params;

    public Instruction(Action action, JSONObject params) {
        this.action = action;
        this.params = params;
    }
    public Instruction(Action action) {
        this.action = action;
        this.params = new JSONObject();
    }

    public Action getAction() {
        return this.action;
    }

    public JSONObject getParameters() {
        return this.params;
    }
}
