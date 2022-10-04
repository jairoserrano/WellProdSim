package WorldModel.agents.world;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import WorldModel.DateSingleton;
import WorldModel.agents.messages.world.WorldMessage;
import WorldModel.agents.messages.world.WorldMessageType;

/**
 * BESA world's periodic guard, holds the behavior when calling the world agent
 */
public class WorldPeriodicGuard extends PeriodicGuardBESA {

    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        try {
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAid(this.agent.getAid());
            DateSingleton singleton = DateSingleton.getInstance();
            WorldMessage worldMessage = new WorldMessage(WorldMessageType.CROP_OBSERVE,null,singleton.getCurrentDate(), null);
            EventBESA eventBESASend = new EventBESA(WorldGuard.class.getName(),worldMessage);
            ah.sendEvent(eventBESASend);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }
}
