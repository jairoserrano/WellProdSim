package WorldModel.world.layer.temperature;

import WorldModel.automata.core.cell.LayerCellState;
import WorldModel.automata.core.cell.GenericWorldLayerCell;

/**
 * Temperature cell concrete implementation
 */
public class TemperatureCell extends GenericWorldLayerCell<LayerCellState> {


    private String id;


    public TemperatureCell(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
