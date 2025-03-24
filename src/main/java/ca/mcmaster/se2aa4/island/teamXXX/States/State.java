package ca.mcmaster.se2aa4.island.teamXXX.States;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.*;

public abstract class State {
    // This should probably be just the Computer interface, if the members of RescueComputer does
    // indeed matter, we should make Computer be able to retrieve that info, or use an abstract
    // class for computer instead
    protected RescueComputer computer;

    public State(RescueComputer computer) {
        this.computer = computer;
    }

    public abstract Instruction determineNextInstruction(JSONObject droneResponse);
}
