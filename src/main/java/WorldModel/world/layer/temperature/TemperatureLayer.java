package WorldModel.world.layer.temperature;

import WorldModel.automata.core.layer.LayerExecutionParams;
import WorldModel.world.layer.LayerFunctionParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import WorldModel.world.helper.DateHelper;
import WorldModel.world.layer.SimWorldSimpleLayer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Temperature layer concrete implementation
 */
public class TemperatureLayer extends SimWorldSimpleLayer<TemperatureCell> {

    private static final Logger logger = LogManager.getLogger(TemperatureLayer.class);

    public TemperatureLayer(String dataFile) {
        super(dataFile);
        this.cell = new TemperatureCell("tempCell");
    }

    @Override
    public void setupLayer() {}

    @Override
    public void executeLayer() {throw new RuntimeException("Method not implemented");}

    @Override
    public void executeLayer(LayerExecutionParams params) {
        LayerFunctionParams params1 = (LayerFunctionParams) params;
        if(this.cell.getCellState() == null) {
            int monthFromDate = DateHelper.getMonthFromStringDate(params1.getDate());
            double nextTemperatureRate = this.calculateGaussianFromMonthData(monthFromDate);
            this.cell.setCellState(params1.getDate(), new TemperatureCellState(nextTemperatureRate));
        } else {
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern(this.worldConfig.getProperty("date.format"));
            int daysBetweenLastDataAndNewEvent = DateHelper.differenceDaysBetweenTwoDates(this.cell.getDate(),params1.getDate());
            for (int i = 0; i < daysBetweenLastDataAndNewEvent; i++) {
                DateTime previousStateDate = DateHelper.getDateInJoda(this.cell.getDate());
                DateTime previousStateDatePlusOneDay = previousStateDate.plusDays(1);
                int month = previousStateDatePlusOneDay.getMonthOfYear()-1;
                String newDate = dtfOut.print(previousStateDatePlusOneDay);
                this.cell.setCellState(newDate, new TemperatureCellState(this.calculateGaussianFromMonthData(month)));
            }
        }
    }

}
