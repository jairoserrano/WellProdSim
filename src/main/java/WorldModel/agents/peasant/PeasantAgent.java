package WorldModel.agents.peasant;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;

/**
 * BESA peasant agent implementation
 */
public class PeasantAgent extends AgentBESA {
    public PeasantAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {

    }

    @Override
    public void shutdownAgent() {

    }
}
