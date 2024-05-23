import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜雨桐 on 2024/5/19 17:57
 */
// A cell in the grid where agents and cops can be placed
public class Cell {
    private int x, y;
    // status: empty, agent_quiet, agent_active, agent_jailed, cop
    private String occupantStatus;

    private Person occupant;

    List<Cell> cellsInVision = new ArrayList<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupantStatus = "empty";
    }

    // get visible cells from the current cell
    public void initializeCellsInVision() {

        int vision = Params.VISION;
        int gridWidth = Params.GRID_WIDTH;
        int gridHeight = Params.GRID_HEIGHT;

        for (int i = -vision; i <= vision; i++) {
            for (int j = -vision; j <= vision; j++) {
                int newX = x + i;
                int newY = y + j;

                // don't add if out of bounds or is the current cell
                if (newX >= 0 && newX < gridWidth
                        && newY >= 0 && newY < gridHeight
                        && !(i == 0 && j == 0)) {

                    cellsInVision.add(Grid.getCell(newX, newY));
                }
            }
        }
    }


    public void setOccupant(Person person) {
        this.occupant = person;
        if (person == null) {
            this.occupantStatus = "empty";
        } else if (person instanceof Agent) {
            if (((Agent) person).isRebellious()) {
                this.occupantStatus = "agent_active";
            } else if (((Agent) person).isArrested()) {
                this.occupantStatus = "agent_jailed";
            } else {
                this.occupantStatus = "agent_quiet";
            }
        } else if (person instanceof Cop) {
            this.occupantStatus = "cop";
        }
    }

    public Person getOccupant() {
        return occupant;
    }

    public List<Cell> getCellsInVision() {
        return cellsInVision;
    }

    public void setOccupantStatus(String status) {
        this.occupantStatus = status;
    }

    public String getOccupantStatus() {
        return occupantStatus;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
