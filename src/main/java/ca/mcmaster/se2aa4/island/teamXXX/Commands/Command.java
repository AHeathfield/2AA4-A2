package ca.mcmaster.se2aa4.island.teamXXX.Commands;

import org.json.JSONObject;

// TODO might make this abstract class so I can have parameters as an attribute
// TODO might need to do liskov substitution, some Commands do not need parameters
// ex. Fly, Stop don't need it
// Another idea is to use method overloading only problem is we would have to overload
public interface Command {
    // TODO change input parameter to something else I'm not sure what yet
    // This allows for extension of many commands
    public JSONObject execute(JSONObject parameters);
}
