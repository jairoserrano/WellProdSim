package WorldModel.agents.peasant;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Util.PeriodicDataBESA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import WorldModel.agents.messages.peasant.PeasantMessage;
import WorldModel.agents.messages.world.WorldMessage;
import WorldModel.agents.messages.world.WorldMessageType;
import WorldModel.agents.world.WorldGuard;
import org.json.JSONObject;

import java.util.Random;

/**
 * BESA peasant's guard, holds the logic to react if the crop has water disease or stress among other logic
 */
public class PeasantGuard extends GuardBESA {

    private static final String WORLD_AGENT_ID = "worldsito";
    private static final Logger logger = LogManager.getLogger(PeasantGuard.class);

    private Random random = new Random();
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        PeasantMessage peasantMessage = (PeasantMessage) eventBESA.getData();
        PeasantState peasantState = (PeasantState) this.agent.getState();
        switch (peasantMessage.getPeasantMessageType()) {
            case REQUEST_CROP_INFORMATION:
                logger.info("Message received: Request crop info");
                WorldMessage worldMessage = new WorldMessage(
                        WorldMessageType.CROP_INFORMATION,
                        peasantState.getCropId(),
                        peasantMessage.getDate(),
                        this.agent.getAid());
                this.sendWorldAgentMessage(worldMessage);
                break;
            case CROP_INFORMATION_NOTIFICATION:
                logger.info("Crop info received: "+peasantMessage.getPayload());
                JSONObject jsonCropResponse = new JSONObject(peasantMessage.getPayload());
                if(jsonCropResponse.getBoolean("waterStress")) {
                    this.decideToWaterCrop(peasantState, peasantMessage.getDate());
                }
                if(jsonCropResponse.getBoolean("disease")) {
                    this.decideIfPesticide(peasantState, peasantMessage.getDate());
                }
                if(jsonCropResponse.getBoolean("cropHarvestReady")) {
                    this.harvestTheCrop();
                }
                break;
            case NOTIFY_CROP_DISEASE:
                logger.info("Disease notification received..."+peasantMessage.getDate());
                this.decideIfPesticide(peasantState, peasantMessage.getDate());
                break;
            case NOTIFY_CROP_WATER_STRESS:
                logger.info("Water stress notification received..."+peasantMessage.getDate());
                this.decideToWaterCrop(peasantState, peasantMessage.getDate());
                break;
            case NOTIFY_CROP_READY_HARVEST:
                logger.info("Crop ready to be harvested..."+peasantMessage.getDate());
                this.harvestTheCrop();
                break;
        }
    }

    private void sendWorldAgentMessage(WorldMessage worldMessage) {
        try{
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAlias(WORLD_AGENT_ID);
            EventBESA event = new EventBESA(WorldGuard.class.getName(),worldMessage);
            ah.sendEvent(event);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    private void decideToWaterCrop(PeasantState peasantState, String date) {
        if(this.random.nextDouble() <= peasantState.getProbabilityOfWaterCropIfWaterStress()) {
            logger.info("Sending irrigation to crop...");
            try{
                AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAlias(WORLD_AGENT_ID);
                WorldMessage worldMessage = new WorldMessage(WorldMessageType.CROP_IRRIGATION,peasantState.getCropId(),date,this.agent.getAid());
                EventBESA event = new EventBESA(WorldGuard.class.getName(),worldMessage);
                ah.sendEvent(event);
            }catch (ExceptionBESA exceptionBESA) {
                exceptionBESA.printStackTrace();
            }
        } else {
            logger.info("No irrigation this time boi");
        }
    }

    private void decideIfPesticide(PeasantState peasantState, String date) {
        if(this.random.nextDouble() <= peasantState.getProbabilityOfPesticideIfDisease()) {
            logger.info("Sending pesticide to crop...");
            try{
                AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAlias(WORLD_AGENT_ID);
                WorldMessage worldMessage = new WorldMessage(WorldMessageType.CROP_PESTICIDE,peasantState.getCropId(),date,this.agent.getAid());
                EventBESA event = new EventBESA(WorldGuard.class.getName(),worldMessage);
                ah.sendEvent(event);
            }catch (ExceptionBESA exceptionBESA) {
                exceptionBESA.printStackTrace();
            }
        } else {
            logger.info("No pesticide this time boi");
        }
    }

    private void harvestTheCrop() {
        logger.info("Harvesting the crop...");
        try{
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAlias(WORLD_AGENT_ID);
            WorldMessage worldMessage = new WorldMessage(WorldMessageType.CROP_HARVEST,null,null,null);
            EventBESA event = new EventBESA(WorldGuard.class.getName(),worldMessage);
            ah.sendEvent(event);
            // Stopping the periodic guard
            this.stopPeriodicGuard();
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    private void stopPeriodicGuard() {
        try{
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAid(this.agent.getAid());
            PeriodicDataBESA periodicDataBESA = new PeriodicDataBESA(PeriodicGuardBESA.STOP_CALL);
            EventBESA eventPeriodic = new EventBESA(PeasantPeriodicGuard.class.getName(), periodicDataBESA);
            ah.sendEvent(eventPeriodic);
        } catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }
}
