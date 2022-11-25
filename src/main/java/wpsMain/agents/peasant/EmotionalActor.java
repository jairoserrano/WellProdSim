package wpsMain.agents.peasant;

import wpsMain.agents.peasant.Personality.EmotionElementType;

import java.util.List;

public class EmotionalActor {
    protected final EmotionalState emotionalState;
    protected final Personality personality;

    public EmotionalActor() {
        this.emotionalState = new EmotionalState();
        this.personality = new Personality();
    }

    public void addEmotionAxis(EmotionalAxis ea) {
        emotionalState.addEmotionAxis(ea.clone());
    }

    public void setPersonRelationship(String person, String relationship) {
        checkItemInSemanticDictionary(EmotionElementType.Person, relationship);
        personality.setPersonRelationship(person, relationship);
    }

    public void setObjectRelationship(String object, String relationship) {
        checkItemInSemanticDictionary(EmotionElementType.Object, relationship);
        personality.setObjectRelationship(object, relationship);
    }

    public void setEventDesirability(String event, String desirability) {
        checkItemInSemanticDictionary(EmotionElementType.Event, desirability);
        personality.setEventDesirability(event, desirability);
    }

    public void processEmotionalEvent(EmotionalEvent ev) {
        float i = estimateEmotionIntensity(ev);
        emotionalState.updateEmotions(ev.getEvent(), i);
        emotionalStateChanged();
    }

    private float estimateEmotionIntensity(EmotionalEvent ev) {
        Float person = personality.getElementSemanticValue(EmotionElementType.Person, ev.getPerson());
        Float event = personality.getElementSemanticValue(EmotionElementType.Event, ev.getEvent());
        Float object = personality.getElementSemanticValue(EmotionElementType.Object, ev.getObject());

        person = (person == null) ? 0 : person;
        event = (event == null) ? 0 : event;
        object = (object == null) ? 0 : object;

        float intensity = EmotionalAxisUtils.Config.PersonWeight * Math.abs(person)
                + EmotionalAxisUtils.Config.EventWeight * Math.abs(event)
                + EmotionalAxisUtils.Config.ObjectWeight * Math.abs(object);
        boolean valence = estimateValence(person, event, object);
        //System.out.println("P:" + person + " E:" + event + " O:" + object);
        //System.out.println("Val: " + valence);
        intensity = (valence ? 1 : -1) * intensity;
        return intensity;
    }

    private boolean estimateValence(Float person, Float event, Float object) {
        boolean v = false;

        person = (person == null || person == 0) ? 1 : person;
        event = (event == null || event == 0) ? 1 : event;
        object = (object == null || object == 0) ? 1 : object;

        person = person / Math.abs(person);
        event = event / Math.abs(event);
        object = object / Math.abs(object);

        //System.out.println("Valence P:" + person + " E:" + event + " O:" + object);
        if ((person.equals(event) && event.equals(object))
                || (person.equals(1f) && event.equals(-1f) && object.equals(-1f))) {
            v = true;
        }

        return v;
    }

    public void emotionalStateChanged() {

    }

    public EmotionalAxis getMostActivatedEmotion() {
        return this.emotionalState.getMostActivatedEmotion();
    }

    public List<EmotionalAxis> getEmotionsListCopy() {
        return this.emotionalState.getEmotionsListCopy();
    }

    @Override
    public String toString() {
        return personality.toString()
                + "\r\n" + emotionalState.toString();
    }

    private void checkItemInSemanticDictionary(EmotionElementType type, String key) {
        Float v = SemanticDictionary.getInstance().getSemanticValue(type, key);
        if (v == null) {
            String typeName = "Personas";
            if (type == EmotionElementType.Event) {
                typeName = "Eventos";
            } else if (type == EmotionElementType.Object) {
                typeName = "Objetos";
            }
            String msg = "El diccionario semántico de " + typeName + " no contiene un item con el nombre " + key;
        }
    }

    public EmotionalAxis getEmotionAxis(String positiveName, String negativeName) {
        return emotionalState.getEmotion(positiveName, negativeName);
    }
}
