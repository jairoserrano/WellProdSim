package wpsMain.agents.peasant;

import BESA.Kernel.Agent.Event.DataBESA;

import java.util.ArrayList;

public class ProductiveSuggestion extends DataBESA {

    String adviserName;

    ArrayList<Resource> resources;

    public String getAdviserName() {
        return adviserName;
    }

    public void setAdviserName(String adviserName) {
        this.adviserName = adviserName;
    }

    public ArrayList<Resource> getSuggestedProducts() {
        return resources;
    }

    public void setSuggestedProducts(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public ProductiveSuggestion(String adviserName, ArrayList<Resource> resources) {
        this.adviserName = adviserName;
        this.resources = resources;
    }
}
