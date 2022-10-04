package WorldModel;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Util.PeriodicDataBESA;
import WorldModel.agents.messages.world.WorldMessage;
import WorldModel.agents.messages.world.WorldMessageType;
import WorldModel.agents.peasant.PeasantGuard;
import WorldModel.agents.peasant.PeasantState;
import WorldModel.agents.world.WorldGuard;
import WorldModel.agents.world.WorldState;
import WorldModel.util.WorldConfiguration;
import WorldModel.world.helper.Hemisphere;
import WorldModel.world.helper.Soil;
import WorldModel.world.layer.crop.CropLayer;
import WorldModel.world.layer.crop.cell.rice.RiceCell;
import WorldModel.world.layer.disease.DiseaseCell;
import WorldModel.world.layer.disease.DiseaseLayer;
import WorldModel.world.layer.evapotranspiration.EvapotranspirationLayer;
import WorldModel.world.layer.shortWaveRadiation.ShortWaveRadiationLayer;
import WorldModel.world.layer.temperature.TemperatureLayer;
import WorldModel.agents.peasant.PeasantAgent;
import WorldModel.agents.peasant.PeasantPeriodicGuard;
import WorldModel.agents.world.WorldAgent;
import WorldModel.agents.world.WorldPeriodicGuard;
import WorldModel.world.layer.rainfall.RainfallLayer;


/**
 * Hello world!
 *
 */
public class App 
{
    private static final String MAIN_RICE_CROP_ID = "rice_1";
    private static final double PSSWD = 0.91;

    private static long DAY_LENGTH = 50;
    private static long CHECK_CROP_STATUS_PERIODIC = 7;
    private static final String INIT_SIMULATION_DATE = "15/03/2022";

    public static void main( String[] args )
    {
        String peasantType = args[0];
        String rainfallConditions = args[1];
        String perturbation = args[2];
        setPerturbation(perturbation);


        // set init date simulation
        DateSingleton.getInstance().setCurrentDate(INIT_SIMULATION_DATE);

        // init agents
        AdmBESA adm = AdmBESA.getInstance();
        PeasantAgent peasantAgent = getPeasant(peasantType);
        peasantAgent.start();
        WorldAgent worldAgent = buildWorld(getRainfallFile(rainfallConditions), peasantAgent.getAid());
        worldAgent.start();
        //----- Init world layers state message
        initialWorldStateInitialization(worldAgent);

        // Initialize periodic guard for World agent, every 8 days crop information and notify peasant
        try{
            AgHandlerBESA ah = adm.getHandlerByAid(worldAgent.getAid());
            PeriodicDataBESA periodicDataBESAWorld = new PeriodicDataBESA(DAY_LENGTH*CHECK_CROP_STATUS_PERIODIC, PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA eventPeriodicWorld = new EventBESA(WorldPeriodicGuard.class.getName(), periodicDataBESAWorld);
            ah.sendEvent(eventPeriodicWorld);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }

        // Initialize periodic guard for peasant agent, every day events
        try{
            AgHandlerBESA ah = adm.getHandlerByAid(peasantAgent.getAid());
            PeriodicDataBESA periodicDataBESA = new PeriodicDataBESA(DAY_LENGTH, PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA eventPeriodic = new EventBESA(PeasantPeriodicGuard.class.getName(), periodicDataBESA);
            ah.sendEvent(eventPeriodic);
        } catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
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
            EventBESA eventBesa = new EventBESA(WorldGuard.class.getName(),worldMessage);
            ah.sendEvent(eventBesa);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    private static WorldAgent buildWorld(String rainfallFile, String agentId) {
        WorldState worldState = buildWorldState(rainfallFile, agentId);
        StructBESA structBESA = new StructBESA();
        structBESA.bindGuard(WorldGuard.class);
        try {
            WorldAgent worldAgent = new WorldAgent("worldsito", worldState,structBESA,PSSWD);
            return worldAgent;
        }catch (ExceptionBESA ex) {
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
                MAIN_RICE_CROP_ID,
                agentId
        ));
        cropLayer.bindLayer("radiation", radiationLayer);
        cropLayer.bindLayer("rainfall", rainfallLayer);
        cropLayer.bindLayer("temperature", temperatureLayer);
        cropLayer.bindLayer("evapotranspiration", evapotranspirationLayer);
        return new WorldState(temperatureLayer,radiationLayer,cropLayer,diseaseLayer,evapotranspirationLayer,rainfallLayer);
    }

    private static PeasantAgent getPeasant(String arg) {
        PeasantAgent peasantAgent = null;
        switch (arg) {
            case "normal":
                peasantAgent = buildNormalPeasantAgent();
                break;
            case "lazy":
                peasantAgent = buildLazyPeasantAgent();
                break;
            case "pro":
                peasantAgent = buildProPeasantAgent();
                break;
            default:
                peasantAgent = buildNormalPeasantAgent();
                break;
        }
        return peasantAgent;
    }

    private static PeasantAgent buildProPeasantAgent() {
        PeasantState peasantState = new PeasantState(
                MAIN_RICE_CROP_ID,
                0.75,
                0.65,
                0.6
        );
        StructBESA structBESA= new StructBESA();
        structBESA.bindGuard(PeasantPeriodicGuard.class);
        structBESA.bindGuard(PeasantGuard.class);
        try {
            PeasantAgent peasantAgent = new PeasantAgent("peasantPro",peasantState, structBESA, PSSWD);
            return peasantAgent;
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        return null;
    }

    private static PeasantAgent buildLazyPeasantAgent() {
        PeasantState peasantState = new PeasantState(
                MAIN_RICE_CROP_ID,
                0.35,
                0,
                0.2
        );
        StructBESA structBESA= new StructBESA();
        structBESA.bindGuard(PeasantPeriodicGuard.class);
        structBESA.bindGuard(PeasantGuard.class);
        try {
            PeasantAgent peasantAgent = new PeasantAgent("peasantLazy",peasantState, structBESA, PSSWD);
            return peasantAgent;
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        return null;
    }

    private static PeasantAgent buildNormalPeasantAgent() {
        PeasantState peasantState = new PeasantState(
                MAIN_RICE_CROP_ID,
                0.55,
                0.5,
                0.5
        );
        StructBESA structBESA= new StructBESA();
        structBESA.bindGuard(PeasantPeriodicGuard.class);
        structBESA.bindGuard(PeasantGuard.class);
        try {
            PeasantAgent peasantAgent = new PeasantAgent("peasantNormal",peasantState, structBESA, PSSWD);
            return peasantAgent;
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
        }
        return null;
    }

    private static String getRainfallFile (String arg) {
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
                worldConfiguration.setPerturbations(true, false); break;
            case "course":
                worldConfiguration.setPerturbations(false, true); break;
            case "all":
                worldConfiguration.setPerturbations(true, true); break;
            default:
                worldConfiguration.setPerturbations(false, false); break;
        }
    }
}
