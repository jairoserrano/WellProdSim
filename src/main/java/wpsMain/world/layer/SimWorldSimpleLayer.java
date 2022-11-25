package wpsMain.world.layer;

import wpsMain.automata.core.layer.GenericWorldLayerUniqueCell;
import wpsMain.world.helper.MonthlyDataLoader;
import wpsMain.world.layer.data.MonthData;
import wpsMain.automata.core.cell.LayerCell;
import wpsMain.util.WorldConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Abstract implementation for the layers, used for this specific world simulation
 * @param <C> type of cell
 */
public abstract class SimWorldSimpleLayer<C extends LayerCell> extends GenericWorldLayerUniqueCell<C> {

    protected List<MonthData> monthlyData;

    protected Random random;

    protected WorldConfiguration worldConfig =  WorldConfiguration.getPropsInstance();

    public SimWorldSimpleLayer(String dataFile) {
        this.loadYearDataFromFile(dataFile);
        this.random = new Random();
    }

    protected double calculateGaussianFromMonthData(int month) {
        MonthData monthData = this.monthlyData.get(month);
        return this.random.nextGaussian()*monthData.getStandardDeviation() + monthData.getAverage();
    }

    protected void loadYearDataFromFile(String dataFile) {
        try{
            this.monthlyData = MonthlyDataLoader.loadMonthlyDataFile(dataFile);
        } catch(IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

}
