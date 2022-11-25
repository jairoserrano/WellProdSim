package wpsMain.agents.peasant;

import java.util.ArrayList;

public class PeasantResources {
    private String id;
    private String name;
    private ArrayList<Resource> resources;

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public PeasantResources(String id, String name, ArrayList<Resource> resources) {
        this.id = id;
        this.name = name;
        this.resources = resources;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return toStringRecursive(this, 0);
    }

    private String toStringRecursive(PeasantResources peasantResources, int nivel) {
        String response = "";
        for (int i = 0; i < nivel; i++) {
            response += "\t";
        }
        response += peasantResources.getId() + " - " + peasantResources.getName() + "\n";

        for (Resource resource : peasantResources.resources) {
            for (int i = 0; i < nivel + 1; i++) {
                response += "\t";
            }
            response += resource;
        }
        return response;
    }

}
