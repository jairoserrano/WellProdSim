package wpsMain.world.layer.crop.cell.action;

import wpsMain.automata.core.cell.LayerCellAction;

/**
 * Crop cell action implementation, holds the payload, action type and date for an action to be taken into account during the layer execution
 */
public class CropCellAction implements LayerCellAction {
    private CropCellActionType actionType;
    private String payload;

    private String date;

    public CropCellAction(CropCellActionType actionType, String payload, String date) {
        this.actionType = actionType;
        this.payload = payload;
        this.date = date;
    }

    public CropCellActionType getActionType() {
        return actionType;
    }

    public void setActionType(CropCellActionType actionType) {
        this.actionType = actionType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
