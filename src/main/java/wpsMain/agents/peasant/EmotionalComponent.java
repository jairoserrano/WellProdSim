package wpsMain.agents.peasant;

public class EmotionalComponent extends EmotionalActor {
    EmotionalComponent(EmotionalComponent emotionalComponent) {

    }

    public EmotionalComponent() {

    }

    public void addEmotionalAxis(String positiveAxis, String negativeAxis, float currentValue, float baseline, float attenuationFactor) {
        EmotionalAxis emotionalAxis = new EmotionalAxis(positiveAxis, negativeAxis, currentValue, baseline, attenuationFactor);
        this.addEmotionAxis(emotionalAxis);
    }

    public void configureEventInfluence(String positiveAxis, String negativeAxis, String event, float influenceFactor) {
        EmotionalAxis emotionalAxis = this.getEmotionAxis(positiveAxis, negativeAxis);
        if (emotionalAxis != null) {
            emotionalAxis.setEventInfluence(event, influenceFactor);
        }
    }

    public void configureEventDesire(String event, String value) {
        this.setEventDesirability(event, value);
    }

    public void configurarRelacionConObjeto(String objeto, String valoracion) {
        this.setObjectRelationship(objeto, valoracion);
    }

    public void configurarRelacionConPersona(String persona, String valoracion) {
        this.setPersonRelationship(persona, valoracion);
    }


    private void configurarDiccionario() {

    }
}
