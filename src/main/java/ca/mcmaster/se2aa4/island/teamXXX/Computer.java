package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

// This will be the brains that determines the next action
public interface Computer {
    public void processData(JSONObject data);
    public Instruction determineNextInstruction();
}
