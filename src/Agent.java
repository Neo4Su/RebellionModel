import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.exp;
import static java.lang.Math.floor;


// Agents
public class Agent extends Person {
    private boolean rebellious;
    private boolean arrested;
    private int jailTime;

    //parameters to calculate rebellion probability
    private double perceivedHardship;
    private double grievance;
    private double riskAversion;

    private double netRisk;


    public Agent(String name, int x, int y) {
        super(name, x, y);
        rebellious = false;
        arrested = false;
        jailTime = 0;

        riskAversion = new Random().nextDouble();
        perceivedHardship = new Random().nextDouble();
        grievance = perceivedHardship * (1 - Params.LEGITIMACY);
    }


    // decide whether to rebel
    public boolean evaluateRebellion() {
        List<Cell> cellsInVision = Grid.getCell(getX(), getY()).getCellsInVision();

        int copCount = 0, activeAgentCount = 0;
        for (Cell cell : cellsInVision) {
            if (cell.getOccupantStatus().equals("cop")) {
                copCount++;
            } else if (cell.getOccupantStatus().equals("agent_active")) {
                activeAgentCount++;
            }
        }

        // no zero division
        if (activeAgentCount == 0) {
            activeAgentCount = 1;
        }

        //double arrestProbability = (activeAgentCount > copCount) ? 0 : 0.99; //netlogo's implementation

        // epstein model
        double arrestProbability = 1 - exp(-2.3 * floor((copCount) / activeAgentCount));

        netRisk = arrestProbability * riskAversion;

        return grievance - netRisk > Params.THRESHOLD;
    }


    public boolean isRebellious() {
        return rebellious;
    }

    public void setRebellious(boolean rebellious) {
        this.rebellious = rebellious;

        if (rebellious) {
            Grid.getCell(getX(), getY()).setOccupantStatus("agent_active");
            //System.out.println(getName() + " is rebelling!");

        } else {
            Grid.getCell(getX(), getY()).setOccupantStatus("agent_quiet");
            //System.out.println(getName() + " quiet down.");
        }
    }

    public boolean isArrested() {
        return arrested;
    }

    public void setArrested(boolean arrested) {
        this.arrested = arrested;

        if (arrested) {

            this.jailTime = new Random().nextInt(Params.MAX_JAIL_TERM);
            this.rebellious = false;
            Grid.getCell(getX(), getY()).setOccupantStatus("agent_jailed");
        } else {
            this.jailTime = 0;

            Grid.getCell(getX(), getY()).setOccupantStatus("agent_quiet");
        }
    }


    public void decreaseJailTime() {
        jailTime--;
        if (jailTime <= 0) {

            Cell currentCell = Grid.getCell(getX(), getY());

            // method 1: release the agent if the current cell is not occupied
            if (currentCell.getOccupantStatus().equals("agent_jailed")
                    || currentCell.getOccupantStatus().equals("empty")) {

                arrested = false;
                rebellious = false;
                System.out.println(getName() + " is released from jail!");
                Grid.getCell(getX(), getY()).setOccupant(this);
            }

/*            // method 2: release the agent if there is an empty cell within vision

            if (currentCell.getOccupantStatus().equals("agent_jailed")
                    || currentCell.getOccupantStatus().equals("empty")) {

                arrested = false;
                rebellious = false;
                System.out.println(getName() + " is released from jail!");
                Grid.getCell(getX(), getY()).setOccupant(this);
                return;
            }

            List<Cell> cellsInVision = currentCell.getCellsInVision();
            List<Cell> emptyCells = new ArrayList<>();
            for (Cell cell : cellsInVision) {
                if (cell.getOccupantStatus().equals("empty")
                        || cell.getOccupantStatus().equals("agent_jailed")) {
                    emptyCells.add(cell);
                }
            }

            Cell[][] cells = Grid.getAllCells();
            if (!emptyCells.isEmpty()) {
                Cell newCell = emptyCells.get(new Random().nextInt(emptyCells.size()));
                arrested = false;
                rebellious = false;
                System.out.println(getName() + " is released from jail!");


                newCell.setOccupant(this);
                setPosition(newCell.getX(), newCell.getY());
            }*/

        }

    }

    @Override
    public String toString() {
        return super.toString() + "[Rebellious: " + rebellious + "]";
    }

}
