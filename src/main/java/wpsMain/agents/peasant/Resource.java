package wpsMain.agents.peasant;

public class Resource {
    private String name;

    public Resource(String name) {
        this.name = name;
    }

    public Resource(Resource resource) {
        this.name = resource.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Resource: " + name;
    }
}
