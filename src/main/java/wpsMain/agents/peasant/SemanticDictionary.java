package wpsMain.agents.peasant;

import wpsMain.agents.peasant.Personality.EmotionElementType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SemanticDictionary {
    private static SemanticDictionary instance = null;

    private final Map<String, SemanticValue> objectRelationships;
    private final Map<String, SemanticValue> personRelationships;
    private final Map<String, SemanticValue> eventDesirability;

    private SemanticDictionary() {
        objectRelationships = new HashMap<>();
        personRelationships = new HashMap<>();
        eventDesirability = new HashMap<>();
    }

    public synchronized static SemanticDictionary getInstance() {
        if (instance == null) {
            instance = new SemanticDictionary();
        }
        return instance;
    }

    private Map getList(EmotionElementType t) {
        if (t.equals(EmotionElementType.Object)) {
            return this.objectRelationships;
        } else if (t.equals(EmotionElementType.Person)) {
            return this.personRelationships;
        } else if (t.equals(EmotionElementType.Event)) {
            return this.eventDesirability;
        }
        return null;
    }

    public void addSemanticItem(EmotionElementType t, SemanticValue s) {
        getList(t).put(s.getName(), s);
    }

    public Collection getSemanticItemList(EmotionElementType t) {
        return getList(t).values();
    }

    public Float getSemanticValue(EmotionElementType t, String name) {
        Object o = getList(t).get(name);
        if (o != null) {
            return ((SemanticValue) o).getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        String str = "Objects" + " -> " + objectRelationships.toString()
                + "\r\nPersons" + " -> " + personRelationships.toString()
                + "\r\nEvents" + " -> " + eventDesirability.toString();
        return str;

    }
}
