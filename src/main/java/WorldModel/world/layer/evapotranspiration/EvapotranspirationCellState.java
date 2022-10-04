package WorldModel.world.layer.evapotranspiration;

import WorldModel.automata.core.cell.LayerCellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Concrete implementation of the evapotranspiration cell state
 */
public class EvapotranspirationCellState implements LayerCellState {

    private static final Logger logger = LogManager.getLogger(EvapotranspirationCellState.class);
    private double evapotranspirationReference;

    public EvapotranspirationCellState(double evapotranspirationReference) {
        logger.info("Next evapotranspiration state: "+evapotranspirationReference);
        this.evapotranspirationReference = evapotranspirationReference;
    }

    public EvapotranspirationCellState(){}

    public double getEvapotranspirationReference() {
        return evapotranspirationReference;
    }

    public void setEvapotranspirationReference(double evapotranspirationReference) {
        this.evapotranspirationReference = evapotranspirationReference;
    }

}
