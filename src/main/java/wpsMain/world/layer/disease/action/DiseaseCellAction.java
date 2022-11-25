package wpsMain.world.layer.disease.action;

import wpsMain.automata.core.cell.LayerCellAction;

/**
 * Implementation of an action that can be taken into account in the layer execution ej: insecticide application to crop
 */
public class DiseaseCellAction implements LayerCellAction {
    String payload;
    String date;

    public DiseaseCellAction(String payload, String date) {
        this.payload = payload;
        this.date = date;
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
