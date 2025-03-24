package ca.mcmaster.se2aa4.island.teamXXX;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;

import org.json.JSONObject;

// Idea behind controller is that they hold a common pressButton which can take any command
// But they will also have their own methods for common commands which will reduce object
// creation (won't have to do like pressButton(new FlyCommand())) which is inefficient
public interface Controller {
    // Command is an interface following the command pattern
    public JSONObject pressButton(Action action, JSONObject parameters);
}
