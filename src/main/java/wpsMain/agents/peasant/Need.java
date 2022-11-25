package wpsMain.agents.peasant;

import wpsMain.agents.peasant.Const.Semantica;

public class Need {
    private Resource resource;
    private EmotionalComponent emotionalComponent;
    private EmotionalAxis emotionalAxis;

    public Need(Resource resource, EmotionalComponent emotionalComponent, float intensity) {
        this.resource = resource;
        this.emotionalComponent = emotionalComponent;
        emotionalAxis = new EmotionalAxis(Semantica.Emociones.Felicidad + resource.getName(), Semantica.Emociones.Tristeza + resource.getName(), intensity, 0.0f, 0.01f);
        emotionalComponent.addEmotionAxis(emotionalAxis);
    }

    public void setIntensity(float intensity) {
        emotionalAxis = new EmotionalAxis(Semantica.Emociones.Felicidad + resource.getName(), Semantica.Emociones.Tristeza + resource.getName(), intensity, 0.0f, 0.01f);
        emotionalComponent.addEmotionAxis(emotionalAxis);
    }

    public Need(Need need) {
        if (need != null) {
            this.resource = null;
            if (need.resource != null) {
                this.resource = new Resource(need.resource);
            }
            this.emotionalComponent = need.emotionalComponent;
        }
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public double getIntensity() {
        return emotionalAxis.getCurrentValue();
    }

    @Override
    public String toString() {
        return "Necesidades: \n" + resource + " Intensidad: " + getIntensity();
    }
}
