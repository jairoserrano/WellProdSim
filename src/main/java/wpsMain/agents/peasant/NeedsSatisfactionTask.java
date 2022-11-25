package wpsMain.agents.peasant;

import rational.mapping.Believes;
import rational.mapping.Task;

public class NeedsSatisfactionTask extends Task {
    @Override
    public boolean checkFinish(Believes believes) {
        return false;
    }

    @Override
    public void executeTask(Believes believes) {

    }

    @Override
    public void interruptTask(Believes believes) {
        System.out.println("Tarea Interrumpida "+((PeasantBDIBelieves)believes).getAlias()+" SatisfacerNecesidadesTask");
    }

    @Override
    public void cancelTask(Believes believes) {

    }
}
