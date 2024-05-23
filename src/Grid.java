import java.util.*;

// the board on which the agents and cops are placed and moved
public class Grid {
    public static Cell[][] cells;
    private static ArrayList<Person> personList = new ArrayList<>();


    // initialize the grid with cells
    public static void initializeGrid() {
        cells = new Cell[Params.GRID_WIDTH][Params.GRID_HEIGHT];
        for (int i = 0; i < Params.GRID_WIDTH; i++) {
            for (int j = 0; j < Params.GRID_HEIGHT; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }

        //fill the grid with agents and cops
        int totalCells = Params.GRID_WIDTH * Params.GRID_HEIGHT;
        int totalAgents = (int) (totalCells * Params.AGENT_DENSITY);
        int totalCops = (int) (totalCells * Params.COP_DENSITY);
        fillGrid(totalAgents, totalCops);

        System.out.println(totalAgents + " agents and " + totalCops + " cops are generated" +
                " on a " + Params.GRID_WIDTH + "x" + Params.GRID_HEIGHT + " grid.");
    }

    // set up the vision of each cell
    public static void setupCellLists() {
        for (int i = 0; i < Params.GRID_WIDTH; i++) {
            for (int j = 0; j < Params.GRID_HEIGHT; j++) {
                cells[i][j].initializeCellsInVision();
            }
        }
    }

    //fill the grid with agents and cops
    private static void fillGrid(int agentsNum, int CopsNum) {
        // generate a random list of positions to place agents and cops
        List<int[]> allPositions = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < Params.GRID_WIDTH; i++) {
            for (int j = 0; j < Params.GRID_HEIGHT; j++) {
                allPositions.add(new int[]{i, j});
            }
        }
        Collections.shuffle(allPositions);

        int i = 0;
        // place agents and cops on the grid
        for (; i < agentsNum; i++) {
            int[] position = allPositions.get(i);
            int x = position[0];
            int y = position[1];
            Person occupant;
            occupant = new Agent("Agent" + i, x, y);
            cells[x][y].setOccupant(occupant);
            personList.add(occupant);

        }
        for (; i < agentsNum + CopsNum; i++) {
            int[] position = allPositions.get(i);
            int x = position[0];
            int y = position[1];
            Person occupant;
            occupant = new Cop("Cop" + i, x, y);
            cells[x][y].setOccupant(occupant);
            personList.add(occupant);
        }

    }

    // move a person from one cell to another
    public static void movePerson(int x, int y, int newX, int newY) {
        cells[newX][newY].setOccupant(cells[x][y].getOccupant());
        cells[x][y].setOccupant(null);

    }

    // getters and setters
    public static ArrayList<Person> getPersonList() {
        return personList;
    }

    public static Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public static void setCell(int x, int y, Person occupant) {
        cells[x][y].setOccupant(occupant);
    }
}
