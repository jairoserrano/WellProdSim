package wpsMain.agents.peasant;

import BESA.BDI.AgentStructuralModel.Agent.AgentBDI;
import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import rational.RationalRole;
import rational.RationalState;
import rational.guards.InformationFlowGuard;
import rational.mapping.Believes;
import rational.mapping.Plan;

import java.util.ArrayList;
import java.util.List;

/**
 * BESA BDI Peasant Agent
 */
public class PeasantBDIAgent extends AgentBDI {
    public PeasantBDIAgent(String alias, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA, ExceptionBESA {
        super(alias, createBelieves(alias), createGoals(), passwd, createStruct(new StructBESA()));
    }

    @Override
    public void setupAgentBDI() {

    }

    @Override
    public void shutdownAgentBDI() {

    }

    private static PeasantBDIBelieves createBelieves(String alias) {

        // New Resources List
        ArrayList<Resource> resources = new ArrayList();
        resources.add(new Resource("arroz"));
        resources.add(new Resource("dinero"));

        // New peasantResources List
        PeasantResources peasantResources = new PeasantResources("1","production", resources);

        // Set first arraylist of interests
        ArrayList<Interest> randomInterest = new ArrayList();
        randomInterest.add(new Interest(peasantResources,1));
        return new PeasantBDIBelieves(alias, randomInterest);
    }


    public void sparkBDI() {
        try {
            AgHandlerBESA agHandler = AdmBESA.getInstance().getHandlerByAlias(this.getAlias());
            // TODO: Ajustar el impulso real
            ArrayList<Resource> resources = new ArrayList();
            resources.add(new Resource("arroz"));
            ProductiveSuggestion productiveSuggestion = new ProductiveSuggestion("inicial",resources );
            //
            EventBESA eventBesa = new EventBESA(InformationFlowGuard.class.getName(), productiveSuggestion);
            agHandler.sendEvent(eventBesa);
        } catch (Exception e) {
            //LogAuditoria.getInstance().escribirError(this.getClass().getName(), this.getAlias(), e);
        }
    }

    public Believes getBelieves() {
        return ((RationalState) this.getState()).getBelieves();
    }

    private static StructBESA createStruct(StructBESA structBESA) {

        try {
            structBESA.addBehavior("IniciarLogroDeMetasGuard");
            structBESA.bindGuard("IniciarLogroDeMetasGuard", StartGoalCompletionGuard.class);
            structBESA.addBehavior("RecibirMensajesPromocionalesGuard");
            structBESA.bindGuard("RecibirMensajesPromocionalesGuard", StartLaborintheLandGuard.class);
        } catch (ExceptionBESA e) {
            throw new RuntimeException(e);
        }
        return structBESA;
    }

    private static List<GoalBDI> createGoals() {
        List<GoalBDI> PeasantBDIBelievesList = new ArrayList();
        NeedsSatisfactionTask needsSatisfactionTask = new NeedsSatisfactionTask();
        Plan needsSatisfactionPlan = new Plan();
        needsSatisfactionPlan.addTask(needsSatisfactionTask);
        RationalRole needsSatisfactionRole = new RationalRole("satisfacerNecesidadesRole", needsSatisfactionPlan);
        NeedsSatisfactionGoalBDI needsSatisfactionGoalBDI = new NeedsSatisfactionGoalBDI(0, needsSatisfactionRole, "SatisfacerNecesidadesGoalBDI", GoalBDITypes.DUTY);
        PeasantBDIBelievesList.add(needsSatisfactionGoalBDI);

        return PeasantBDIBelievesList;
    }

}
