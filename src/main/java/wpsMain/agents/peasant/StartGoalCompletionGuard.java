package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;

public class StartGoalCompletionGuard extends PeriodicGuardBESA {
    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        System.out.println("Start Goal!");
        ((PeasantBDIAgent)this.getAgent()).sparkBDI();
    }
}
