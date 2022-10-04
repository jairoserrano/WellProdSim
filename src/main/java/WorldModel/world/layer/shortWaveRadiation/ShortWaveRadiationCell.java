package WorldModel.world.layer.shortWaveRadiation;

import WorldModel.automata.core.cell.GenericWorldLayerCell;

/**
 * Concrete cell implementation
 */
public class ShortWaveRadiationCell extends GenericWorldLayerCell<ShortWaveRadiationCellState> {

    private String id;

    public ShortWaveRadiationCell(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

}
