package wpsMain.agents.peasant;

public class Interest {
    private PeasantResources peasantResources;
    private double intensity;

    public Interest(PeasantResources peasantResources, double intensity) {
        this.peasantResources = peasantResources;
        this.intensity = intensity;
    }

    public PeasantResources getPeasantResources() {
        return peasantResources;
    }

    public void setPeasantResources(PeasantResources peasantResources) {
        this.peasantResources = peasantResources;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "Intereses: " + peasantResources.getName() + "\n Intensidad: " + intensity;
    }
}
