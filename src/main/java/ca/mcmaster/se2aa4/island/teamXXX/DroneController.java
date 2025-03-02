package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.EnumMap;
import org.json.JSONObject;

public class DroneController implements Controller {
    private EnumMap<Action, Command> commandMap = new EnumMap<>(Action.class);


    public DroneController() {
        // Adding commands for this controller
        addCommand(Action.STOP, new StopCommand());
        // TODO add rest of commands
    }
    
    // This is how all commands will be sent to drone
    @Override
    public JSONObject pressButton(Action action, JSONObject parameters) 
        throws IllegalArgumentException {
        // 
        if (this.commandMap.containsKey(action)) {
            Command command = this.commandMap.get(action);
            return command.execute(parameters);
        }
        else {
            throw new IllegalArgumentException("Action: " + action + ", is not a valid action.");
        }
    }

    public void addCommand(Action action, Command command) {
        this.commandMap.put(action, command);
    }    

}
