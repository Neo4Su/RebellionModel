import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Cops
 * Extension: Instead of moving randomly each turn
 *, cops will move to a cell with higher density of active agents
*/
public class Cop extends Person {
    public Cop(String name, int x, int y) {
        super(name, x, y);
    }


    // Move to a cell with higher density of active agents
    @Override
    public synchronized void moveWithinVision(){
        List<Cell> cellsInVision = Grid.getCell(getX(), getY()).getCellsInVision();
        List<Cell> availableCells = new ArrayList<>();

        Cell targetCell = null;
        int maxActiveAgentCount = 0;

        // find available cells
        for (Cell cell : cellsInVision) {
            if (cell.getOccupantStatus().equals("empty")
                    || cell.getOccupantStatus().equals("agent_jailed")) {
                availableCells.add(cell);
            }
        }

        // find the cell with the highest density of active agents
        for (Cell cell : availableCells) {
            int activeAgentCount = 0;
            List<Cell> neighboringCells = cell.getCellsInVision();
            for (Cell neighbor : neighboringCells) {
                if (neighbor.getOccupantStatus().equals("agent_active")) {
                    activeAgentCount++;
                }
            }

            // update target cell
            if (activeAgentCount > maxActiveAgentCount) {
                maxActiveAgentCount = activeAgentCount;
                targetCell = cell;
            }
        }

        // move to the target cell, if it does not exist, move randomly
        if (targetCell != null) {
            Grid.movePerson(getX(), getY(), targetCell.getX(), targetCell.getY());
            setPosition(targetCell.getX(), targetCell.getY());
        } else {
            super.moveWithinVision();
        }
    }



    public void checkAndArrest() {
        List<Cell> cellsInVision = Grid.getCell(getX(), getY()).getCellsInVision();
        int x = getX(), y = getY();
        List<Agent> activeAgentsInVision = new ArrayList<>();
        for (Cell cell : cellsInVision) {
            if (cell.getOccupantStatus().equals("agent_active")) {

                if (!(cell.getOccupant() instanceof Agent)) {

                    System.err.println("Error: Occupant is not an agent! " + cell.getX() + "," + cell.getY());
                }

                activeAgentsInVision.add((Agent) cell.getOccupant());
            }
        }

        // If there are active agents, randomly select one and arrest it
        if (!activeAgentsInVision.isEmpty()) {

            Random random = new Random();
            Agent target = activeAgentsInVision.get(random.nextInt(activeAgentsInVision.size()));
            arrest(target);

            System.out.println(getName() + " arrests " + target.getName() +
                    " and move to " + target.getX() + "," + target.getY());

            // Move the cop to the position of the jailed agent
            Grid.movePerson(getX(), getY(), target.getX(), target.getY());

            setPosition(target.getX(), target.getY());

        }
    }

    public void arrest(Agent target) {
        if (!target.isRebellious()) System.err.println("Error: Trying to arrest a non-rebellious agent!");


        target.setArrested(true);


        System.out.println(target.getName() + " " + target.getX() + "," + target.getY() + " is arrested!");

    }
}
