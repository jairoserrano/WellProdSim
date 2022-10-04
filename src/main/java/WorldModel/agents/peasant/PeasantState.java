package WorldModel.agents.peasant;

import BESA.Kernel.Agent.StateBESA;

import java.util.HashSet;
import java.util.Set;

/**
 * Peasant state, holds all the necessary attributes for this simulation
 */
public class PeasantState extends StateBESA {
    /**
     * Crop associated to the peasant
     */
    private String cropId;
    /**
     * Probability that a peasant reviews the crop state for a given day
     */
    private double probabilityOfDailyCropSupervision;

    /**
     * Probability of the peasant to water the crop if it has water stress
     */
    private double probabilityOfWaterCropIfWaterStress;

    /**
     * Probability of the peasant to water the crop if it has a disease
     */
    private double probabilityOfPesticideIfDisease;

    /**
     * Data structure that holds the months that the peasant has taken courses
     */
    private Set<Integer> monthsTakenCourse = new HashSet<>();


    public PeasantState(String cropId, double probabilityOfDailyCropSupervision, double probabilityOfWaterCropIfWaterStress, double probabilityOfPesticideIfDisease) {
        this.cropId = cropId;
        this.probabilityOfDailyCropSupervision = probabilityOfDailyCropSupervision;
        this.probabilityOfWaterCropIfWaterStress = probabilityOfWaterCropIfWaterStress;
        this.probabilityOfPesticideIfDisease = probabilityOfPesticideIfDisease;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public double getProbabilityOfDailyCropSupervision() {
        return probabilityOfDailyCropSupervision;
    }

    public void setProbabilityOfDailyCropSupervision(double probabilityOfDailyCropSupervision) {
        this.probabilityOfDailyCropSupervision = probabilityOfDailyCropSupervision;
    }

    public Set<Integer> getMonthsTakenCourse() {
        return monthsTakenCourse;
    }

    public void addMonth(int month) {
        this.monthsTakenCourse.add(month);
    }

    public double getProbabilityOfWaterCropIfWaterStress() {
        return probabilityOfWaterCropIfWaterStress;
    }

    public void setProbabilityOfWaterCropIfWaterStress(double probabilityOfWaterCropIfWaterStress) {
        this.probabilityOfWaterCropIfWaterStress = probabilityOfWaterCropIfWaterStress;
    }

    public void setMonthsTakenCourse(Set<Integer> monthsTakenCourse) {
        this.monthsTakenCourse = monthsTakenCourse;
    }

    public double getProbabilityOfPesticideIfDisease() {
        return probabilityOfPesticideIfDisease;
    }

    public void setProbabilityOfPesticideIfDisease(double probabilityOfPesticideIfDisease) {
        this.probabilityOfPesticideIfDisease = probabilityOfPesticideIfDisease;
    }
}
