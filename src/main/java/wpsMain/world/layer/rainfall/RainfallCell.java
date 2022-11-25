package wpsMain.world.layer.rainfall;

import wpsMain.automata.core.cell.GenericWorldLayerCell;

/**
 * Concrete implementation of the rainfall cell
 */
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
