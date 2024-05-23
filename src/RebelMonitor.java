import java.util.ArrayList;
import java.util.Collections;


// execute update rules each turn
public class RebelMonitor {
    private ArrayList<Person> personList;
    private DataManager dataManager = new DataManager();
    private int timeStep = 0;

    public void update() {
        // record agents data
        int quietAgentsCount = 0;
        int activeAgentsCount = 0;
        int jailedAgentsCount = 0;
        for (Person person : personList) {
            if (person instanceof Agent) {
                Agent agent = (Agent) person;
                if (agent.isArrested()) {
                    jailedAgentsCount++;
                } else if (agent.isRebellious()) {
                    activeAgentsCount++;
                } else {
                    quietAgentsCount++;
                }
            }
        }
        dataManager.recordData(timeStep, quietAgentsCount, activeAgentsCount, jailedAgentsCount);

        timeStep++;
        System.out.println("--------------------");
        System.out.println("Time step: " + timeStep);

        // shuffle the list to randomize the order of update
        Collections.shuffle(personList);

        // apply update rules to each person
        for (Person person : personList) {
            if (person instanceof Agent) {
                Agent agent = (Agent) person;

                if (!agent.isArrested()) {
                    // move the agent
                    if (Params.MOVEMENT_AGENTS) {
                        agent.moveWithinVision();
                    }

                    agent.setRebellious(agent.evaluateRebellion());

                } else {
                    agent.decreaseJailTime();
                }
            } else if (person instanceof Cop) {
                Cop cop = (Cop) person;
                // move the cop
                if (Params.MOVEMENT_COPS) {
                    cop.moveWithinVision();
                }
                cop.checkAndArrest();
            }
        }

    }

    public RebelMonitor(ArrayList<Person> personList) {
        this.personList = personList;
    }

    public ArrayList<Person> getPersonList() {
        return personList;
    }
}
