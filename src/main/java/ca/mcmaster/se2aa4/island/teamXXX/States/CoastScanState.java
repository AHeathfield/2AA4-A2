package ca.mcmaster.se2aa4.island.teamXXX.States;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import ca.mcmaster.se2aa4.island.teamXXX.Enums.*;
import ca.mcmaster.se2aa4.island.teamXXX.States.*;
import ca.mcmaster.se2aa4.island.teamXXX.*;

public class CoastScanState extends State {
    private final Logger logger = LogManager.getLogger();

    // Constructor
    public CoastScanState(RescueComputer computer) {
        super(computer);
    }

    @Override
    public Instruction determineNextInstruction() {
        JSONObject param = new JSONObject();

        computer.setCurrentState(new CoastSearchState(computer));
        return new Instruction(Action.SCAN, param);
    }
}
