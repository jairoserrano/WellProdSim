package wpsMain.world.layer.crop.cell;

import wpsMain.automata.core.cell.LayerCellState;

/**
 * Implementation for a generic crop cell state, holds the attributes that change over time
 */
public class CropCellState implements LayerCellState {
    protected double evapotranspiration;
    protected double growingDegreeDays;
    protected double aboveGroundBiomass;

    protected double cumulatedEvapotranspiration;

    protected double depletionFractionAdjusted;

    protected double rootZoneDepletionAtTheEndOfDay;

    protected boolean waterStress;



    public CropCellState(
            double riceEvapotranspiration,
            double currentGrowingDegreeDays,
            double currentAboveGroundBiomass,
            double cumulatedEvapotranspiration,
            double depletionFractionAdjusted,
            double rootZoneDepletionAtTheEndOfDay,
            boolean waterStress
    ) {
        this.evapotranspiration = riceEvapotranspiration;
        this.growingDegreeDays = currentGrowingDegreeDays;
        this.aboveGroundBiomass = currentAboveGroundBiomass;
        this.cumulatedEvapotranspiration = cumulatedEvapotranspiration;
        this.depletionFractionAdjusted = depletionFractionAdjusted;
        this.rootZoneDepletionAtTheEndOfDay = rootZoneDepletionAtTheEndOfDay;
        this.waterStress = waterStress;
    }

    public CropCellState() {
    }

    public double getEvapotranspiration() {
        return evapotranspiration;
    }

    public void setEvapotranspiration(double evapotranspiration) {
        this.evapotranspiration = evapotranspiration;
    }

    public double getGrowingDegreeDays() {
        return growingDegreeDays;
    }

    public void setGrowingDegreeDays(double growingDegreeDays) {
        this.growingDegreeDays = growingDegreeDays;
    }

    public double getAboveGroundBiomass() {
        return aboveGroundBiomass;
    }

    public void setAboveGroundBiomass(double aboveGroundBiomass) {
        this.aboveGroundBiomass = aboveGroundBiomass;
    }

    public double getCumulatedEvapotranspiration() {
        return cumulatedEvapotranspiration;
    }

    public void setCumulatedEvapotranspiration(double cumulatedEvapotranspiration) {
        this.cumulatedEvapotranspiration = cumulatedEvapotranspiration;
    }

    public double getDepletionFractionAdjusted() {
        return depletionFractionAdjusted;
    }
    public void setDepletionFractionAdjusted(double depletionFractionAdjusted) {
        this.depletionFractionAdjusted = depletionFractionAdjusted;
    }

    public double getRootZoneDepletionAtTheEndOfDay() {
        return rootZoneDepletionAtTheEndOfDay;
    }

    public void setRootZoneDepletionAtTheEndOfDay(double rootZoneDepletionAtTheEndOfDay) {
        this.rootZoneDepletionAtTheEndOfDay = rootZoneDepletionAtTheEndOfDay;
    }

    public boolean isWaterStress() {
        return waterStress;
    }

    public void setWaterStress(boolean waterStress) {
        this.waterStress = waterStress;
    }
}
