package wpsMain.agents.world;

import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;

/**
 * AgentBESA implementation for the world agent
 */
public class WorldAgent extends AgentBESA {
    public WorldAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    @Override
    public void setupAgent() {
    }

    @Override
    public void shutdownAgent() {

    }
}
