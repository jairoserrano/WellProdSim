package org.javeriana.world.layer.rainfall;

import org.javeriana.automata.core.cell.GenericWorldLayerCell;

public class RainfallCell extends GenericWorldLayerCell<RainfallCellState> {

    private String id;

    public RainfallCell(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

}
