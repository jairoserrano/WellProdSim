package wpsMain.agents.peasant;

import rational.data.InfoData;
import rational.mapping.Believes;

import java.util.ArrayList;
import java.util.HashMap;

public class PeasantBDIBelieves implements Believes {

    private String alias;
    private ArrayList<Interest> interest;
    private ArrayList<Need> unsatisfiedNeeds;
    private Need currentNeed;
    private HashMap<String, Integer> goalState;
    private EmotionalComponent emotionalComponent;

    @Override
    public boolean update(InfoData infoData) {
        return true;
    }

    @Override
    public Believes clone() {
        return null;
    }

    public String getAlias() {
        return alias;
    }

    public PeasantBDIBelieves(String alias, ArrayList<Interest> interest) {

        this.alias = alias;
        this.emotionalComponent = new EmotionalComponent();

        configureSemanticDictionary();
        configureEmotionalComponent();

        // TODO: Ajustar necesidades iniciales
        ArrayList<Need> randomNeeds = new ArrayList();

        randomNeeds.add(new Need(new Resource("arroz"), new EmotionalComponent(), 1));

        this.unsatisfiedNeeds = new ArrayList();
        for (Need randomNeed : randomNeeds) {
            addUnsatisfiedNeeds(randomNeed);
        }
        this.interest = interest;
        this.goalState = new HashMap();
        //System.out.println(PeasantBDIBelieves.class.getName(), alias, this.toString());
    }

    public void addUnsatisfiedNeeds(Need unsatisfiedNeed) {
        if (unsatisfiedNeed != null) {
            Resource resource = unsatisfiedNeed.getResource();
            emotionalComponent.addEmotionalAxis(Const.Semantica.Emociones.Felicidad + resource.getName(), Const.Semantica.Emociones.Tristeza + resource.getName(), (float) unsatisfiedNeed.getIntensity(), 0.0f, 0.01f);
            emotionalComponent.configureEventInfluence(Const.Semantica.Emociones.Felicidad + resource.getName(), Const.Semantica.Emociones.Tristeza + resource.getName(), Const.Semantica.Eventos.RecibePublicidad, 1.0f);
            emotionalComponent.configureEventDesire(Const.Semantica.Eventos.RecibePublicidad + resource.getName(), Const.Semantica.ValoracionEventos._4_AlgoDeseable.getName());
            unsatisfiedNeeds.add(unsatisfiedNeed);
        }
    }

    public HashMap<String, Integer> getGoalState() {
        return goalState;
    }

    public int getGoalState(String goalName) {
        if (!goalState.containsKey(goalName)) {
            if (goalName.equals(NeedsSatisfactionGoalBDI.class.getName())) {
                goalState.put(goalName, NeedsSatisfactionGoalBDI.ESTADO_SIN_INICIAR);
            }
        }
        return goalState.get(goalName);
    }

    public void setGoalStates(HashMap<String, Integer> goalState) {
        this.goalState = goalState;
    }

    public void setGoalState(String goal, int state) {
        String stateName = "DESCONOCIDO";
        String goalName = "DESCONOCIDO";
        ArrayList<String> nombreYEstado = getGoalNameAndStatus(goal, state);
        if (nombreYEstado != null) {
            if (nombreYEstado.size() == 2) {
                goalName = nombreYEstado.get(0);
                stateName = nombreYEstado.get(1);
            }
        }
        System.out.println("Cambia Estado Meta " + alias + " " + goalName + " " + stateName);
        goalState.put(goal, state);
    }

    public ArrayList<String> getGoalNameAndStatus(String goal, int status) {
        ArrayList<String> goalNameAndStatus = new ArrayList();
        if (goal.equals(NeedsSatisfactionGoalBDI.class.getName())) {
            goalNameAndStatus.add("NeedsSatisfactionGoalBDI");
            switch (status) {
                case NeedsSatisfactionGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD:
                    goalNameAndStatus.add("ESTADO_EJECUCION_ASIGNANDO_NECESIDAD");
                    break;
                case NeedsSatisfactionGoalBDI.ESTADO_FINALIZADA:
                    goalNameAndStatus.add("ESTADO_FINALIZADA");
                    break;
            }
        }
        return goalNameAndStatus;
    }

    public Need getCurrentNeed() {
        return currentNeed;
    }

    public ArrayList<Need> getUnsatisfiedNeeds() {
        return unsatisfiedNeeds;
    }

    public void toFarming(ProductiveSuggestion productiveSuggestions) {
        if (productiveSuggestions != null) {
            ArrayList<Resource> totalProductiveSuggestions = productiveSuggestions.getSuggestedProducts();
            if (totalProductiveSuggestions != null) {
                for (Resource suggestedResources : totalProductiveSuggestions) {
                    if (unsatisfiedNeeds != null) {
                        boolean currentNeed = false;
                        // TODO: adicionar acción a necesidad insatisfecha
                    }
                }
            }
        }
    }

    private static void configureSemanticDictionary() {
        SemanticDictionary sd = SemanticDictionary.getInstance();
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._1_Repulsivo);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._2_NoValioso);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._3_Indiferente);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._4_Valioso);
        sd.addSemanticItem(Personality.EmotionElementType.Object, Const.Semantica.ValoracionObjetos._5_Importante);

        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._1_Enemigo);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._2_NoAmigable);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._3_Desconocido);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._4_Amigo);
        sd.addSemanticItem(Personality.EmotionElementType.Person, Const.Semantica.ValoracionPersonas._5_Cercano);

        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._1_Indeseable);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._2_AlgoIndeseable);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._3_Indiferente);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._4_AlgoDeseable);
        sd.addSemanticItem(Personality.EmotionElementType.Event, Const.Semantica.ValoracionEventos._5_Deseable);
    }

    private void configureEmotionalComponent() {
        this.emotionalComponent = new EmotionalComponent();

        emotionalComponent.addEmotionalAxis(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, 0.0f, 0.0f, 0.05f);
        emotionalComponent.configureEventInfluence(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.ObservaCompra, 0.1f);
        emotionalComponent.configureEventInfluence(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.RecibePublicidad, 0.8f);
        emotionalComponent.configureEventInfluence(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.ObservaSuciedad, 0.5f);
        emotionalComponent.configureEventInfluence(Const.Semantica.Emociones.Felicidad, Const.Semantica.Emociones.Tristeza, Const.Semantica.Eventos.ObservaRobo, 0.7f);

        emotionalComponent.configureEventDesire(Const.Semantica.Eventos.RecibePublicidad, Const.Semantica.ValoracionEventos._4_AlgoDeseable.getName());
        emotionalComponent.configureEventDesire(Const.Semantica.Eventos.ObservaCompra, Const.Semantica.ValoracionEventos._5_Deseable.getName());
        emotionalComponent.configureEventDesire(Const.Semantica.Eventos.ObservaRobo, Const.Semantica.ValoracionEventos._1_Indeseable.getName());
        emotionalComponent.configureEventDesire(Const.Semantica.Eventos.ObservaSuciedad, Const.Semantica.ValoracionEventos._1_Indeseable.getName());
        emotionalComponent.configureEventDesire(Const.Semantica.Eventos.SeAlimenta, Const.Semantica.ValoracionEventos._5_Deseable.getName());
        emotionalComponent.configureEventDesire(Const.Semantica.Eventos.UtilizaSaniario, Const.Semantica.ValoracionEventos._5_Deseable.getName());

        emotionalComponent.configurarRelacionConObjeto(Const.Semantica.Objetos.Producto, Const.Semantica.ValoracionObjetos._1_Repulsivo.getName());
        emotionalComponent.configurarRelacionConObjeto(Const.Semantica.Objetos.Almacen, Const.Semantica.ValoracionObjetos._4_Valioso.getName());
        emotionalComponent.configurarRelacionConObjeto(Const.Semantica.Objetos.Sanitario, Const.Semantica.ValoracionObjetos._5_Importante.getName());
        emotionalComponent.configurarRelacionConObjeto(Const.Semantica.Objetos.Alimento, Const.Semantica.ValoracionObjetos._5_Importante.getName());

        emotionalComponent.configurarRelacionConPersona(Const.Semantica.Personas.Comprador, Const.Semantica.ValoracionPersonas._3_Desconocido.getName());
        emotionalComponent.configurarRelacionConPersona(Const.Semantica.Personas.Transeunte, Const.Semantica.ValoracionPersonas._3_Desconocido.getName());
        emotionalComponent.configurarRelacionConPersona(Const.Semantica.Personas.Vendedor, Const.Semantica.ValoracionPersonas._1_Enemigo.getName());
        emotionalComponent.configurarRelacionConPersona(Const.Semantica.Personas.Ladron, Const.Semantica.ValoracionPersonas._1_Enemigo.getName());
    }

}
