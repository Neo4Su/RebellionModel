import javax.swing.*;
import java.awt.*;


// GUI
public class MapUI extends JPanel {
    private static final int GRID_SIZE = 20;
    private RebelMonitor monitor;

    public MapUI(RebelMonitor monitor) {
        this.monitor = monitor;
        this.setPreferredSize(new Dimension(Params.GRID_WIDTH * GRID_SIZE,
                Params.GRID_HEIGHT * GRID_SIZE));
    }

    // draw the grid, agents and cops
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw grid
        for (int i = 0; i < Params.GRID_WIDTH; i++) {
            for (int j = 0; j < Params.GRID_HEIGHT; j++) {
                g.drawRect(i * GRID_SIZE, j * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }

        // draw agents and cops on the grid
        for (Person person : monitor.getPersonList()) {
            if (person instanceof Agent) {
                Agent agent = (Agent) person;
                if (agent.isArrested()) {
                    g.setColor(Color.GRAY);
                } else if (agent.isRebellious()) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.GREEN);
                }
            } else if (person instanceof Cop) {
                g.setColor(Color.BLUE);
            }
            g.fillOval(person.getX() * GRID_SIZE,
                    person.getY() * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        }
    }
}
