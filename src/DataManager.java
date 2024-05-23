import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Record data and export to csv
public class DataManager {
    private FileWriter csvWriter;

    // Initialize the CSV file
    public DataManager() {
        try {
            csvWriter = new FileWriter("data.csv");

            // Add metadata to the CSV file
            csvWriter.append("Parameters\n");
            csvWriter.append("Initial Cop Density," + Params.COP_DENSITY + "\n");
            csvWriter.append("Initial Agent Density," + Params.AGENT_DENSITY + "\n");
            csvWriter.append("Vision," + Params.VISION + "\n");
            csvWriter.append("Max Jail Term," + Params.MAX_JAIL_TERM + "\n");
            csvWriter.append("Government Legitimacy," + Params.LEGITIMACY + "\n");
            csvWriter.append("Movement Agents," + Params.MOVEMENT_AGENTS + "\n");
            csvWriter.append("Movement Cops," + Params.MOVEMENT_COPS + "\n");
            csvWriter.append("Width," + Params.GRID_WIDTH + "\n");
            csvWriter.append("Height," + Params.GRID_HEIGHT + "\n");
            csvWriter.append("\n");

            String[] headers = {"time_step", "quiet", "active", "jailed"};
            csvWriter.append(String.join(",", headers));
            csvWriter.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Write data to the CSV file
    public void recordData(int timeStep, int quietAgents, int activeAgents, int jailedAgents) {
        try {
            String[] row = {String.valueOf(timeStep), String.valueOf(quietAgents),
                    String.valueOf(activeAgents), String.valueOf(jailedAgents)};
            csvWriter.append(String.join(",", row));
            csvWriter.append("\n");
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
