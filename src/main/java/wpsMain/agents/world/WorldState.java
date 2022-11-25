package wpsMain.agents.world;

import BESA.Kernel.Agent.StateBESA;
import wpsMain.world.layer.LayerFunctionParams;
import wpsMain.world.layer.disease.DiseaseLayer;
import wpsMain.world.layer.evapotranspiration.EvapotranspirationLayer;
import wpsMain.world.layer.shortWaveRadiation.ShortWaveRadiationLayer;
import wpsMain.world.layer.temperature.TemperatureLayer;
import wpsMain.world.LayerExecutor;
import wpsMain.world.layer.crop.CropLayer;
import wpsMain.world.layer.rainfall.RainfallLayer;

/**
 * Class that holds the world state, in this case the cellular automaton layers
 */
public class WorldState extends StateBESA {
    private TemperatureLayer temperatureLayer;
    private ShortWaveRadiationLayer shortWaveRadiationLayer;
    private CropLayer cropLayer;
    private DiseaseLayer diseaseLayer;
    private EvapotranspirationLayer evapotranspirationLayer;
    private RainfallLayer rainfallLayer;
    private LayerExecutor layerExecutor = new LayerExecutor();

    public WorldState(
            TemperatureLayer temperatureLayer,
            ShortWaveRadiationLayer shortWaveRadiationLayer,
            CropLayer cropLayer,
            DiseaseLayer diseaseLayer,
            EvapotranspirationLayer evapotranspirationLayer,
            RainfallLayer rainfallLayer) {
        this.temperatureLayer = temperatureLayer;
        this.shortWaveRadiationLayer = shortWaveRadiationLayer;
        this.cropLayer = cropLayer;
        this.diseaseLayer = diseaseLayer;
        this.evapotranspirationLayer = evapotranspirationLayer;
        this.rainfallLayer = rainfallLayer;
        layerExecutor.addLayer(this.shortWaveRadiationLayer,this.temperatureLayer,this.evapotranspirationLayer,this.rainfallLayer,this.diseaseLayer,this.cropLayer);
    }

    public TemperatureLayer getTemperatureLayer() {
        return temperatureLayer;
    }

    public ShortWaveRadiationLayer getShortWaveRadiationLayer() {
        return shortWaveRadiationLayer;
    }

    public CropLayer getCropLayer() {
        return cropLayer;
    }

    public DiseaseLayer getDiseaseLayer() {
        return diseaseLayer;
    }

    public EvapotranspirationLayer getEvapotranspirationLayer() {
        return evapotranspirationLayer;
    }

    public RainfallLayer getRainfallLayer() {
        return rainfallLayer;
    }

    public LayerExecutor getLayerExecutor() {
        return layerExecutor;
    }

    public void lazyUpdateCropsForDate(String eventDate) {
        this.layerExecutor.executeLayers(new LayerFunctionParams(eventDate));
    }
}
