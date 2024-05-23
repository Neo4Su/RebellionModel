import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜雨桐 on 2024/5/19 18:00
 */
// Initialize and run the simulation
public class Rebellion {
    public static void main(String[] args) {
        Grid.initializeGrid();
        Grid.setupCellLists();
        ArrayList<Person> personList = Grid.getPersonList();
        RebelMonitor monitor = new RebelMonitor(personList);


        // Create a frame
        JFrame frame = new JFrame("Rebellion Simulation Group 5");
        MapUI mapUI = new MapUI(monitor);
        frame.add(mapUI);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monitor.update();
                mapUI.repaint();
            }
        });
        timer.start();
    }
}
