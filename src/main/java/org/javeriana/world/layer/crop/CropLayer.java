package org.javeriana.world.layer.crop;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javeriana.automata.core.layer.GenericWorldLayer;
import org.javeriana.automata.core.layer.LayerExecutionParams;
import org.javeriana.util.WorldConfiguration;
import org.javeriana.world.helper.DateHelper;
import org.javeriana.world.layer.LayerFunctionParams;
import org.javeriana.world.layer.crop.cell.CropCell;
import org.javeriana.world.layer.crop.cell.CropCellState;
import org.javeriana.world.layer.disease.DiseaseCellState;
import org.javeriana.world.layer.evapotranspiration.EvapotranspirationCellState;
import org.javeriana.world.layer.evapotranspiration.EvapotranspirationLayer;
import org.javeriana.world.layer.rainfall.RainfallLayer;
import org.javeriana.world.layer.shortWaveRadiation.ShortWaveRadiationCellState;
import org.javeriana.world.layer.shortWaveRadiation.ShortWaveRadiationLayer;
import org.javeriana.world.layer.temperature.TemperatureCellState;
import org.javeriana.world.layer.temperature.TemperatureLayer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CropLayer extends GenericWorldLayer {

    private static final Logger logger = LogManager.getLogger(CropLayer.class);
    private List<CropCell> cropCellList = new ArrayList<>();
    private WorldConfiguration config = WorldConfiguration.getPropsInstance();

    @Override
    public void setupLayer() {}

    @Override
    public void executeLayer() {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public void executeLayer(LayerExecutionParams params) {
        LayerFunctionParams paramsLayer = (LayerFunctionParams) params;
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(this.config.getProperty("date.format"));
        TemperatureLayer temperatureLayer = (TemperatureLayer) this.dependantLayers.get("temperature");
        EvapotranspirationLayer evapotranspirationLayer = (EvapotranspirationLayer) this.dependantLayers.get("evapotranspiration");
        ShortWaveRadiationLayer shortWaveRadiationLayer = (ShortWaveRadiationLayer) this.dependantLayers.get("radiation");
        RainfallLayer rainfallLayer = (RainfallLayer) this.dependantLayers.get("rainfall");
        this.cropCellList.parallelStream().filter(CropCell::isActive).forEach(currentCell -> {
            CropCellState currentState = (CropCellState) currentCell.getCellState();
            if(currentState == null) {
                CropCellState newCellState = new CropCellState();
                newCellState.setEvapotranspiration(currentCell.getCropFactor_ini()*((EvapotranspirationCellState)evapotranspirationLayer.getCell().getCellStateByDate(paramsLayer.getDate())).getEvapotranspirationReference());
                newCellState.setAboveGroundBiomass(0);
                newCellState.setGrowingDegreeDays(((TemperatureCellState)temperatureLayer.getCell().getCellStateByDate(paramsLayer.getDate())).getTemperature());
                currentCell.setCellState(paramsLayer.getDate(),newCellState);
            } else {
                int daysBetweenLastDataAndEvent = DateHelper.differenceDaysBetweenTwoDates(currentCell.getDate(),paramsLayer.getDate());
                for (int i = 0; i < daysBetweenLastDataAndEvent; i++) {
                    CropCellState previousState = (CropCellState) currentCell.getCellState();
                    DateTime previousStateDate = DateHelper.getDateInJoda(currentCell.getDate());
                    DateTime newStateDate = previousStateDate.plusDays(1);
                    String newDate = dtfOut.print(newStateDate);
                    CropCellState newCellState = new CropCellState();
                    newCellState.setGrowingDegreeDays(previousState.getGrowingDegreeDays()+((TemperatureCellState)temperatureLayer.getCell().getCellStateByDate(newDate)).getTemperature());
                    //------------------------------- growing degree days and k_c logic ----------------------------------------
                    double maximumRadiationEfficiency = Double.parseDouble(this.config.getProperty("agb.maximumRadiationEfficiency"));
                    double diseaseDamageCropFactor = Double.parseDouble(this.config.getProperty("disease.damagesCrop"));
                    double agbConversionFactor = Double.parseDouble(this.config.getProperty("agb.conversionFactor"));
                    if(newCellState.getGrowingDegreeDays()<currentCell.getDegreeDays_mid()) {
                        newCellState.setEvapotranspiration(
                                currentCell.getCropFactor_ini()*
                                ((evapotranspirationLayer.getCell().getCellStateByDate(newDate)).getEvapotranspirationReference())*
                                (((DiseaseCellState)currentCell.getDiseaseCell().getCellState()).isInfected() ? diseaseDamageCropFactor : 1)
                                //Missing water stress factor
                        );
                    }else if (newCellState.getGrowingDegreeDays()>=currentCell.getDegreeDays_mid() && currentState.getGrowingDegreeDays()<currentCell.getDegreeDays_end()) {
                        newCellState.setEvapotranspiration(currentCell.getCropFactor_mid()*((EvapotranspirationCellState)evapotranspirationLayer.getCell().getCellStateByDate(newDate)).getEvapotranspirationReference());
                    }else {
                        newCellState.setEvapotranspiration(currentCell.getCropFactor_end()*((EvapotranspirationCellState)evapotranspirationLayer.getCell().getCellStateByDate(newDate)).getEvapotranspirationReference());
                        //------------------------------------ alert peasant crop is ready to collect ---------------------------------------------------
                    }
                    newCellState.setAboveGroundBiomass(previousState.getAboveGroundBiomass()+maximumRadiationEfficiency*newCellState.getEvapotranspiration()*((ShortWaveRadiationCellState)shortWaveRadiationLayer.getCell().getCellStateByDate(newDate)).getShortWaveRadiation()*agbConversionFactor);
                    currentCell.setCellState(newDate, newCellState);
                }
            }
        });
    }

    public void addCrop(CropCell crop) {
        this.cropCellList.add(crop);
    }

    public void writeCropData() {
        String fileDirection = this.config.getProperty("crop.dataFiles");
        for(CropCell cropCell: this.cropCellList) {
            String filename = fileDirection+"\\"+cropCell.getId()+".csv";
            List<String[]> dataLines = new ArrayList<>();
            dataLines.add(new String[]{"date","evapotranspiration","agb"});
            cropCell.getHistoricalData().keySet().stream().forEach(dateKey -> {
                CropCellState stateCell = (CropCellState) cropCell.getHistoricalData().get(dateKey);
                dataLines.add(new String[]{dateKey.toString(),stateCell.getEvapotranspiration()+"",stateCell.getAboveGroundBiomass()+""});
            });
            File csvOutputFile = new File(filename);
            try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
                dataLines.stream()
                        .map(this::convertToCSV)
                        .forEach(pw::println);
            }catch (FileNotFoundException fileNotFoundException){
                logger.error("File not Found!!!");
            }
        }
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
