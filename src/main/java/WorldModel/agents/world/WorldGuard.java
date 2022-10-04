package WorldModel.agents.world;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Util.PeriodicDataBESA;
import WorldModel.agents.messages.peasant.PeasantMessageType;
import WorldModel.agents.messages.world.WorldMessage;
import WorldModel.agents.peasant.PeasantGuard;
import WorldModel.util.WorldConfiguration;
import WorldModel.world.layer.crop.cell.CropCell;
import WorldModel.world.layer.crop.cell.CropCellState;
import WorldModel.world.layer.disease.DiseaseCellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import WorldModel.DateSingleton;
import WorldModel.agents.messages.peasant.PeasantMessage;
import WorldModel.world.layer.crop.CropLayer;
import org.json.JSONObject;

/**
 * BESA world's guard, holds the actions that receive from the peasant agent
 */
public class WorldGuard extends GuardBESA {
    private static final Logger logger = LogManager.getLogger(WorldGuard.class);
    private WorldConfiguration worldConfig =  WorldConfiguration.getPropsInstance();
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        WorldMessage worldMessage = (WorldMessage)eventBESA.getData();
        WorldState worldState = (WorldState) this.agent.getState();
        switch (worldMessage.getWorldMessageType()) {
            case CROP_INIT:
                logger.info("Start event, initialize first layers state");
                worldState.lazyUpdateCropsForDate(worldMessage.getDate());
                DateSingleton.getInstance().getDatePlusOneDayAndUpdate();
                break;
            case CROP_INFORMATION:
                logger.info("Message received: Crop - "+worldMessage.getCropId()+" information "+worldMessage.getPayload()+ " date: "+worldMessage.getDate());
                worldState.lazyUpdateCropsForDate(worldMessage.getDate());
                CropCellState cropCellState = worldState.getCropLayer().getCropStateById(worldMessage.getCropId());
                CropCell cropCellInfo = worldState.getCropLayer().getCropCellById(worldMessage.getCropId());
                DiseaseCellState diseaseCellState = (DiseaseCellState) cropCellInfo.getDiseaseCell().getCellState();
                JSONObject cropDataJson = new JSONObject(cropCellState);
                cropDataJson.put("disease", diseaseCellState.isInfected());
                cropDataJson.put("cropHarvestReady", cropCellInfo.isHarvestReady());
                PeasantMessage peasantMessage = new PeasantMessage(PeasantMessageType.CROP_INFORMATION_NOTIFICATION, worldMessage.getPeasantAgentId(),cropDataJson.toString());
                peasantMessage.setDate(worldMessage.getDate());
                this.replyToPeasantAgent(worldMessage.getPeasantAgentId(), peasantMessage);
                break;
            case CROP_OBSERVE:
                logger.info("Observing crops (lazy mode).... on date: "+worldMessage.getDate());
                worldState.getCropLayer().getAllCrops().forEach(cropCell -> {
                    if(((CropCellState)cropCell.getCellState()).isWaterStress()) {
                        this.notifyPeasantCropProblem(PeasantMessageType.NOTIFY_CROP_WATER_STRESS,cropCell.getAgentPeasantId(),worldMessage.getDate());
                    }
                    if(((DiseaseCellState)cropCell.getDiseaseCell().getCellState()).isInfected()) {
                        this.notifyPeasantCropProblem(PeasantMessageType.NOTIFY_CROP_DISEASE,cropCell.getAgentPeasantId(),worldMessage.getDate());
                    }
                    if(cropCell.isHarvestReady()) {
                        this.notifyPeasantCropReadyToHarvest(cropCell.getAgentPeasantId(),worldMessage.getDate());
                    }
                });
                break;
            case CROP_IRRIGATION:
                // Adding the event of irrigation, will be reflected in the next layers execution
                logger.info("Irrigation on the crop, date: "+worldMessage.getDate());
                String cropIdToIrrigate = worldMessage.getCropId();
                String defaultWaterQuantity = this.worldConfig.getProperty("crop.defaultValuePerIrrigation");
                worldState.getCropLayer().addIrrigationEvent(cropIdToIrrigate, defaultWaterQuantity, worldMessage.getDate());
                break;
            case CROP_PESTICIDE:
                // Adding the event of pesticide, will be reflected in the next layers execution
                logger.info("Adding pesticide to the crop, date: "+worldMessage.getDate());
                String cropIdToAddPesticide = worldMessage.getCropId();
                String defaultCropInsecticideCoverage = this.worldConfig.getProperty("disease.insecticideDefaultCoverage");
                String diseaseCellId = worldState.getCropLayer().getCropCellById(cropIdToAddPesticide).getDiseaseCell().getId();
                worldState.getDiseaseLayer().addInsecticideEvent(diseaseCellId,defaultCropInsecticideCoverage,worldMessage.getDate());
                break;
            case CROP_HARVEST:
                this.harvestCrop(worldState.getCropLayer());
                break;
        }
    }

    public void replyToPeasantAgent(String peasantAgentId, PeasantMessage peasantMessage) {
        try {
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAid(peasantAgentId);
            EventBESA event = new EventBESA(PeasantGuard.class.getName(),peasantMessage);
            ah.sendEvent(event);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    public void notifyPeasantCropProblem(PeasantMessageType messageType, String aid, String date) {
        try {
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAid(aid);
            PeasantMessage peasantMessage = new PeasantMessage(messageType,aid,null);
            peasantMessage.setDate(date);
            EventBESA event = new EventBESA(PeasantGuard.class.getName(),peasantMessage);
            ah.sendEvent(event);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    public void notifyPeasantCropReadyToHarvest(String aid, String date) {
        try {
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAid(aid);
            PeasantMessage peasantMessage = new PeasantMessage(PeasantMessageType.NOTIFY_CROP_READY_HARVEST,aid,null);
            peasantMessage.setDate(date);
            EventBESA event = new EventBESA(PeasantGuard.class.getName(),peasantMessage);
            ah.sendEvent(event);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    public void harvestCrop(CropLayer cropLayer) {
        cropLayer.writeCropData();
        try {
            AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAid(this.agent.getAid());
            PeriodicDataBESA periodicDataBESA = new PeriodicDataBESA(PeriodicGuardBESA.STOP_CALL);
            EventBESA eventPeriodic = new EventBESA(WorldPeriodicGuard.class.getName(),periodicDataBESA);
            ah.sendEvent(eventPeriodic);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }
}
