package wpsMain.world.layer.disease;

import wpsMain.automata.core.cell.GenericWorldLayerCell;

/**
 * Concrete disease cell implementation
 */
public class DiseaseCell extends GenericWorldLayerCell<DiseaseCellState> {

    private String id;
    private double percentageOfCropCoverage = 0;
    private String dateInsecticideApplication;

    public DiseaseCell(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public double getPercentageOfCropCoverage() {
        return percentageOfCropCoverage;
    }

    public void setPercentageOfCropCoverage(double percentageOfCropCoverage) {
        this.percentageOfCropCoverage = percentageOfCropCoverage;
    }

    public String getDateInsecticideApplication() {
        return dateInsecticideApplication;
    }

    public void setDateInsecticideApplication(String dateInsecticideApplication) {
        this.dateInsecticideApplication = dateInsecticideApplication;
    }
}
