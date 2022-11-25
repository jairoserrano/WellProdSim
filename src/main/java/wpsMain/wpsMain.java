package wpsMain;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Util.PeriodicDataBESA;
import wpsMain.agents.messages.world.WorldMessage;
import wpsMain.agents.messages.world.WorldMessageType;
import wpsMain.util.WorldConfiguration;
import wpsMain.agents.peasant.*;
import wpsMain.agents.world.*;
import wpsMain.util.WPSExperimentConfig;
import wpsMain.world.helper.*;
import wpsMain.world.layer.crop.CropLayer;
import wpsMain.world.layer.crop.cell.rice.RiceCell;
import wpsMain.world.layer.disease.DiseaseCell;
import wpsMain.world.layer.disease.DiseaseLayer;
import wpsMain.world.layer.evapotranspiration.EvapotranspirationLayer;
import wpsMain.world.layer.shortWaveRadiation.ShortWaveRadiationLayer;
import wpsMain.world.layer.temperature.TemperatureLayer;
import wpsMain.world.layer.rainfall.RainfallLayer;


/**
 * Hello world!
 */
public class wpsMain {
    private static final double PASSWORD = 0.91;

    public static void main(String[] args) {

        // Set default values of Peasant
        WPSExperimentConfig wpsExperimentConfig = new WPSExperimentConfig(args);

        // Set world perturbation
        setPerturbation(wpsExperimentConfig.getPerturbation());

        // Set init date of simulation
        DateSingleton.getInstance().setCurrentDate(wpsExperimentConfig.getStartSimulationDate());

        // init agents
        AdmBESA adm = AdmBESA.getInstance();
        PeasantBDIAgent peasantBDIAgent = getPeasant(wpsExperimentConfig.getPeasantType());
        peasantBDIAgent.start();
        System.out.println("peasantBDI started");

        WorldAgent worldAgent = buildWorld(getRainfallFile(wpsExperimentConfig.getRainfallConditions()), peasantBDIAgent.getAid());
        worldAgent.start();
        System.out.println("worldAgent started");

        // Init world layers state message
        initialWorldStateInitialization(worldAgent);
        System.out.println("worldAgent initialized");

        // Initialize periodic guard for World agent, every 8 days crop information and notify peasant
        /*try {
            AgHandlerBESA ah = adm.getHandlerByAid(worldAgent.getAid());
            PeriodicDataBESA periodicDataBESAWorld = new PeriodicDataBESA(wpsExperimentConfig.getDayLength() * wpsExperimentConfig.getCheckCropStatusPeriodic(), PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA eventPeriodicWorld = new EventBESA(WorldPeriodicGuard.class.getName(), periodicDataBESAWorld);
            ah.sendEvent(eventPeriodicWorld);
        } catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }*/

    }

    // Triggers an event in order to initialize all the crop and weather layers
    private static void initialWorldStateInitialization(WorldAgent worldAgent) {
        AdmBESA adm = AdmBESA.getInstance();
        WorldMessage worldMessage = new WorldMessage(
                WorldMessageType.CROP_INIT,
                null,
                DateSingleton.getInstance().getCurrentDate(),
                null);
        try {
            AgHandlerBESA ah = adm.getHandlerByAid(worldAgent.getAid());
            EventBESA eventBesa = new EventBESA(WorldGuard.class.getName(), worldMessage);
            ah.sendEvent(eventBesa);
        } catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    private static WorldAgent buildWorld(String rainfallFile, String agentId) {
        WorldState worldState = buildWorldState(rainfallFile, agentId);
        StructBESA structBESA = new StructBESA();
        structBESA.bindGuard(WorldGuard.class);
        try {
            WorldAgent worldAgent = new WorldAgent("worldsito", worldState, structBESA, PASSWORD);
            return worldAgent;
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        return null;
    }

    private static WorldState buildWorldState(String rainfallFile, String agentId) {
        WorldConfiguration worldConfiguration = WorldConfiguration.getPropsInstance();
        ShortWaveRadiationLayer radiationLayer = new ShortWaveRadiationLayer(
                worldConfiguration.getProperty("data.radiation"),
                Hemisphere.SOUTHERN,
                9);
        TemperatureLayer temperatureLayer = new TemperatureLayer(worldConfiguration.getProperty("data.temperature"));
        EvapotranspirationLayer evapotranspirationLayer = new EvapotranspirationLayer(worldConfiguration.getProperty("data.evapotranspiration"));
        RainfallLayer rainfallLayer = new RainfallLayer(rainfallFile);
        DiseaseLayer diseaseLayer = new DiseaseLayer();
        DiseaseCell diseaseCellRice = new DiseaseCell("rice1DiseaseCell");
        diseaseLayer.addVertex(diseaseCellRice);
        CropLayer cropLayer = new CropLayer();
        cropLayer.addCrop(new RiceCell(
                1.05,
                1.2,
                0.7,
                1512,
                3330,
                100,
                0.9,
                0.2,
                Soil.SAND,
                true,
                diseaseCellRice,
                "rice_1",
                agentId
        ));
        cropLayer.bindLayer("radiation", radiationLayer);
        cropLayer.bindLayer("rainfall", rainfallLayer);
        cropLayer.bindLayer("temperature", temperatureLayer);
        cropLayer.bindLayer("evapotranspiration", evapotranspirationLayer);
        return new WorldState(temperatureLayer, radiationLayer, cropLayer, diseaseLayer, evapotranspirationLayer, rainfallLayer);
    }

    private static PeasantBDIAgent getPeasant(String arg) {
        PeasantBDIAgent peasantBDIAgent = null;
        peasantBDIAgent = buildNormalPeasantAgent();
        /**
         switch (arg) {
         case "normal":
         peasantBDIAgent = buildNormalPeasantAgent();
         break;
         case "lazy":
         peasantBDIAgent = buildLazyPeasantAgent();
         break;
         case "pro":
         peasantBDIAgent = buildProPeasantAgent();
         break;
         default:
         peasantBDIAgent = buildNormalPeasantAgent();
         break;
         }*/
        return peasantBDIAgent;
    }

    private static PeasantBDIAgent buildProPeasantAgent() {
        PeasantState peasantState = new PeasantState(
                "rice_1",
                0.75,
                0.65,
                0.6
        );
        StructBESA structBESA = new StructBESA();
        structBESA.bindGuard(PeasantPeriodicGuard.class);
        structBESA.bindGuard(PeasantGuard.class);
        try {
            PeasantBDIAgent peasantBDIAgent = new PeasantBDIAgent("peasantPro", structBESA, PASSWORD);
            return peasantBDIAgent;
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        return null;
    }

    private static PeasantBDIAgent buildLazyPeasantAgent() {
        PeasantState peasantState = new PeasantState(
                "rice_1",
                0.35,
                0,
                0.2
        );
        StructBESA structBESA = new StructBESA();
        structBESA.bindGuard(PeasantPeriodicGuard.class);
        structBESA.bindGuard(PeasantGuard.class);
        try {
            PeasantBDIAgent peasantBDIAgent = new PeasantBDIAgent("peasantLazy", structBESA, PASSWORD);
            return peasantBDIAgent;
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        return null;
    }

    private static PeasantBDIAgent buildNormalPeasantAgent() {
        /*PeasantState peasantState = new PeasantState(
                MAIN_RICE_CROP_ID,
                0.55,
                0.5,
                0.5
        );*/
        StructBESA structBESA = new StructBESA();
        structBESA.bindGuard(PeasantPeriodicGuard.class);
        structBESA.bindGuard(PeasantGuard.class);
        try {
            PeasantBDIAgent peasantBDIAgent = new PeasantBDIAgent("peasantNormal", structBESA, PASSWORD);
            return peasantBDIAgent;
        } catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        return null;
    }

    private static String getRainfallFile(String arg) {
        WorldConfiguration worldConfiguration = WorldConfiguration.getPropsInstance();
        String rainfallFile = "";
        switch (arg) {
            case "wet":
                rainfallFile = worldConfiguration.getProperty("data.rainfall.wet");
                break;
            case "dry":
                rainfallFile = worldConfiguration.getProperty("data.rainfall.dry");
                break;
            case "normal":
                rainfallFile = worldConfiguration.getProperty("data.rainfall");
                break;
            default:
                rainfallFile = worldConfiguration.getProperty("data.rainfall");
                break;
        }
        return rainfallFile;
    }

    private static void setPerturbation(String arg) {
        WorldConfiguration worldConfiguration = WorldConfiguration.getPropsInstance();
        switch (arg) {
            case "disease":
                worldConfiguration.setPerturbations(true, false);
                break;
            case "course":
                worldConfiguration.setPerturbations(false, true);
                break;
            case "all":
                worldConfiguration.setPerturbations(true, true);
                break;
            default:
                worldConfiguration.setPerturbations(false, false);
                break;
        }
    }
}
