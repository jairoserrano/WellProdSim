package wpsMain.agents.peasant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EmotionalState {
    private final List<EmotionalAxis> emotions;

    public EmotionalState() {
        emotions = new ArrayList<>();
    }

    public void addEmotionAxis(EmotionalAxis ea) {
        EmotionalAxis x = getEmotion(ea.getPositiveName(), ea.getNegativeName());
        if (x == null) {
            emotions.add(ea);
        }else{
            x.setCurrentValue(ea.getCurrentValue());
            x.setBaseValue(ea.getBaseValue());
            x.setForgetFactor(ea.getForgetFactor());
            x.setEventInfluences(ea.getEventInfluences());
        }
    }

    public void updateEmotions(String event, float intensity) {
        for (EmotionalAxis e : emotions) {
            e.updateIntensity(event, intensity);
        }
    }

    public EmotionalAxis getEmotion(String positiveName, String negativeName) {
        EmotionalAxis ea = null;
        Iterator itr = emotions.iterator();
        if (itr != null) {
            while (itr.hasNext()) {
                EmotionalAxis e = (EmotionalAxis) itr.next();
                if (e.getPositiveName().toLowerCase().equals(positiveName.toLowerCase())
                        && e.getNegativeName().toLowerCase().equals(negativeName.toLowerCase())) {
                    ea = e;
                    break;
                }
            }
        }
        return ea;
    }

    @Override
    public String toString() {
        return emotions.toString();
    }

    public EmotionalAxis getMostActivatedEmotion() {
        EmotionalAxis ea = null;
        Iterator itr = emotions.iterator();
        if (itr != null) {
            while (itr.hasNext()) {
                EmotionalAxis e = (EmotionalAxis) itr.next();
                if (ea == null || (e.getActivationValue() >= ea.getActivationValue())) {
                    ea = e;
                }
            }
        }
        if (ea != null) {
            return ea.clone();
        } else {
            return null;
        }
    }

    public List<EmotionalAxis> getEmotionsListCopy() {
        List<EmotionalAxis> list = new ArrayList<>();
        Iterator itr = emotions.iterator();
        if (itr != null) {
            while (itr.hasNext()) {
                list.add(((EmotionalAxis) itr.next()).clone());
            }
        }
        return list;
    }
}
