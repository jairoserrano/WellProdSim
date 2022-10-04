package WorldModel.world;

import WorldModel.automata.core.layer.WorldLayer;
import WorldModel.world.layer.LayerFunctionParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Pojo that contains all the world layers and executes them in a specific order
 */
public class LayerExecutor {

    private List<WorldLayer>layers = new ArrayList<>();

    public LayerExecutor() {
    }

    public void addLayer(WorldLayer... layers) {
        this.layers.addAll(Arrays.asList(layers));
    }

    public void executeLayers(LayerFunctionParams params) {
        for(WorldLayer layer: this.layers) {
            layer.executeLayer(params);
        }
    }
}
