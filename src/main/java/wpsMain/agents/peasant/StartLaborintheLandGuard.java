package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;

public class StartLaborintheLandGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA event) {
        System.out.println("Start Spark!");
        ((PeasantBDIBelieves) ((PeasantBDIAgent) this.getAgent()).getBelieves()).toFarming((ProductiveSuggestion) event.getData());
    }
}
