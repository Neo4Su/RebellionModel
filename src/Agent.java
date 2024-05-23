import java.util.List;
import java.util.Random;

/**
 * Created by 杜雨桐 on 2024/5/19 17:48
 */
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

        double arrestProbability = (activeAgentCount > copCount) ? 0 : 0.99;
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

    public int getJailTime() {
        return jailTime;
    }

    public void decreaseJailTime() {

        jailTime--;
        if (jailTime <= 0) {

            // release the agent if the current cell is not occupied
            // TODO: find and move to a empty cell within vision?
            Cell currentCell = Grid.getCell(getX(), getY());
            if (currentCell.getOccupantStatus().equals("agent_jailed")
                    || currentCell.getOccupantStatus().equals("empty")) {

                arrested = false;
                rebellious = false;
                System.out.println(getName() + " is released from jail!");
                Grid.getCell(getX(), getY()).setOccupant(this);
            }
        }

    }

    @Override
    public String toString() {
        return super.toString() + "[Rebellious: " + rebellious + "]";
    }

}
