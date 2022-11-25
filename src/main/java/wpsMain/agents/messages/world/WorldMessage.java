package wpsMain.agents.messages.world;

import BESA.Kernel.Agent.Event.DataBESA;

public class WorldMessage extends DataBESA {
    private WorldMessageType worldMessageType;
    private String cropId;
    private String peasantAgentId;
    private String payload;
    private String date;

    public WorldMessage(WorldMessageType worldMessageType, String cropId, String date, String peasantAgentId) {
        this.worldMessageType = worldMessageType;
        this.cropId = cropId;
        this.date = date;
        this.peasantAgentId = peasantAgentId;
    }

    public String getPeasantAgentId() {
        return peasantAgentId;
    }

    public void setPeasantAgentId(String peasantAgentId) {
        this.peasantAgentId = peasantAgentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WorldMessageType getWorldMessageType() {
        return worldMessageType;
    }

    public void setWorldMessageType(WorldMessageType worldMessageType) {
        this.worldMessageType = worldMessageType;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
