import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// Parent class for agents and cops
public class Person {
    private String name;
    private int x, y;

    public Person(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    // no need for setter functions, because name, x, y will change automatically

    public String getName() {
        return name+"("+x+","+y+")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return name + "[" + x + "," + y + "]";
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized void moveWithinVision() {
        Cell c=Grid.getCell(x,y);
        List<Cell> cellsInVision = Grid.getCell(x,y).getCellsInVision();
        List<Cell> availableCells = new ArrayList<>();


        // find available cells TODO:jailed as available?
        for (Cell cell : cellsInVision) {
            if (cell.getOccupantStatus().equals("empty")
                    || cell.getOccupantStatus().equals("agent_jailed")) {
                availableCells.add(cell);
            }
        }

        // move to a random available cell in vision
        if (!availableCells.isEmpty()) {
            Cell newCell = availableCells.get(new Random().nextInt(availableCells.size()));
            //System.out.println(getName()+" moves to " + newCell.getX() + "," + newCell.getY());
            Grid.movePerson(x, y, newCell.getX(), newCell.getY());
            setPosition(newCell.getX(), newCell.getY());
        }
    }
}
