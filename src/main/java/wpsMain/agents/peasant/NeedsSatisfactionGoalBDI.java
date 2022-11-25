package wpsMain.agents.peasant;

import BESA.BDI.AgentStructuralModel.GoalBDI;
import BESA.BDI.AgentStructuralModel.GoalBDITypes;
import BESA.BDI.AgentStructuralModel.StateBDI;
import BESA.Kernel.Agent.Event.KernellAgentEventExceptionBESA;
import rational.RationalRole;
import rational.mapping.Believes;

import java.util.ArrayList;

public class NeedsSatisfactionGoalBDI extends GoalBDI {
    public static final int ESTADO_SIN_INICIAR = 0;
    public static final int ESTADO_FINALIZADA = 1;
    public static final int ESTADO_EJECUCION_ASIGNANDO_NECESIDAD = 2;

    public NeedsSatisfactionGoalBDI(long id, RationalRole role, String description, GoalBDITypes type) {
        super(id, role, description, type);
    }

    @Override
    public double evaluateViability(Believes believes) throws KernellAgentEventExceptionBESA {
        PeasantBDIBelieves peasantBDIBelieves = (PeasantBDIBelieves) believes;
        int goalState = peasantBDIBelieves.getGoalState(NeedsSatisfactionGoalBDI.class.getName());
        switch (goalState) {
            case NeedsSatisfactionGoalBDI.ESTADO_SIN_INICIAR:
                peasantBDIBelieves.setGoalState(NeedsSatisfactionGoalBDI.class.getName(), NeedsSatisfactionGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD);
                return 1;
            case NeedsSatisfactionGoalBDI.ESTADO_EJECUCION_ASIGNANDO_NECESIDAD:
                return 1;
        }
        return 0;
    }

    @Override
    public double detectGoal(Believes believes) throws KernellAgentEventExceptionBESA {
        Need currentNeed = ((PeasantBDIBelieves) believes).getCurrentNeed();
        if (currentNeed != null) {
            return 1;
        }
        ArrayList unsatisfiedNeeds = ((PeasantBDIBelieves) believes).getUnsatisfiedNeeds();
        if (unsatisfiedNeeds != null) {
            if (unsatisfiedNeeds.size() > 0) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public double evaluatePlausibility(Believes blvs) throws KernellAgentEventExceptionBESA {
        return 1;
    }

    @Override
    public double evaluateContribution(StateBDI stateBDI) throws KernellAgentEventExceptionBESA {
        ArrayList unsatisfiedNeeds = ((PeasantBDIBelieves) stateBDI.getBelieves()).getUnsatisfiedNeeds();
        if (unsatisfiedNeeds != null) {
            if (unsatisfiedNeeds.size() >= 0) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean predictResultUnlegality(StateBDI sbdi) throws KernellAgentEventExceptionBESA {
        return true;
    }

    @Override
    public boolean goalSucceeded(Believes blvs) throws KernellAgentEventExceptionBESA {
        return false;
    }
}
