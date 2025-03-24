package ca.mcmaster.se2aa4.island.teamXXX;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
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

    public void setAction(Action action) {
        this.action = action;
    }
    public void setParameters(JSONObject params) {
        this.params = params;
    }
}
