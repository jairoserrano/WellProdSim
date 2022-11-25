package wpsMain.util;

public class WPSExperimentConfig {

    private String peasantType = "";
    private String rainfallConditions = "";
    private String perturbation = "";
    private String startSimulationDate = "15/03/2022";
    private String mainRiceCropID = "rice_1";
    private long dayLength = 50;
    private long checkCropStatusPeriodic = 7;

    public WPSExperimentConfig(String[] args) {
        // Simulation parameters

        if (args.length == 7) {
            peasantType = args[0];
            rainfallConditions = args[1];
            perturbation = args[2];
            startSimulationDate = args[3];
            mainRiceCropID = args[4];
            //dayLength = Long(args[5]);
            //checkCropStatusPeriodic = args[6];
        } else {
            peasantType = "normal";
            rainfallConditions = "normal";
            perturbation = "none";
            startSimulationDate = "15/03/2022";
            mainRiceCropID = "rice_1";
            dayLength = 50;
            checkCropStatusPeriodic = 7;
        }
    }

    public String getMainRiceCropID() {
        return mainRiceCropID;
    }

    public void setMainRiceCropID(String mainRiceCropID) {
        this.mainRiceCropID = mainRiceCropID;
    }

    public long getDayLength() {
        return dayLength;
    }

    public void setDayLength(long dayLength) {
        this.dayLength = dayLength;
    }

    public long getCheckCropStatusPeriodic() {
        return checkCropStatusPeriodic;
    }

    public void setCheckCropStatusPeriodic(long checkCropStatusPeriodic) {
        this.checkCropStatusPeriodic = checkCropStatusPeriodic;
    }

    public String getStartSimulationDate() {
        return startSimulationDate;
    }

    public void setStartSimulationDate(String startSimulationDate) {
        this.startSimulationDate = startSimulationDate;
    }

    public String getPeasantType() {
        return peasantType;
    }

    public void setPeasantType(String peasantType) {
        this.peasantType = peasantType;
    }

    public String getRainfallConditions() {
        return rainfallConditions;
    }

    public void setRainfallConditions(String rainfallConditions) {
        this.rainfallConditions = rainfallConditions;
    }

    public String getPerturbation() {
        return perturbation;
    }

    public void setPerturbation(String perturbation) {
        this.perturbation = perturbation;
    }

}
