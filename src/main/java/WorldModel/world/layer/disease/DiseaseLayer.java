package WorldModel.world.layer.disease;

import WorldModel.automata.core.layer.GenericWorldLayerGraphCell;
import WorldModel.automata.core.layer.LayerExecutionParams;
import WorldModel.util.WorldConfiguration;
import WorldModel.world.layer.LayerFunctionParams;
import WorldModel.world.layer.disease.action.DiseaseCellAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import WorldModel.world.helper.DateHelper;
import org.jgrapht.Graphs;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Concrete disease layer implementation
 */
public class DiseaseLayer extends GenericWorldLayerGraphCell<DiseaseCell> {

    private static final Logger logger = LogManager.getLogger(DiseaseLayer.class);
    private HashMap<String, DiseaseCell> cellDirectory;
    private WorldConfiguration worldConfig =  WorldConfiguration.getPropsInstance();
    private String currentDateLayerExecution;

    public DiseaseLayer() {
        this.cellDirectory = new HashMap<>();
    }

    /**
     * Adds disease cell to the graph
     * @param cell disease cell
     */
    public void addVertex(DiseaseCell cell) {
        this.cellDirectory.put(cell.getId(),cell);
        this.allCells.add(cell);
        this.simpleCellGraph.addVertex(cell);
    }

    /**
     * Joins 2 disease cells in the simple graph, this represents in real life two crops that are neighbors
     * @param cell1 disease cell 1
     * @param cell2 disease cell 2
     */
    public void addEdge(DiseaseCell cell1, DiseaseCell cell2) {
        this.simpleCellGraph.addEdge(cell1, cell2);
    }

    /**
     * Adds insecticide of a specified crop in the next layer run this will affect probabilities
     * @param cellId id of the cell
     * @param percentageOfCropCoverage percentage of coverage of the crop that was cover with insecticide
     */
    public void addInsecticideToCell(String cellId, double percentageOfCropCoverage, String date){
        DiseaseCell diseaseCell = this.cellDirectory.get(cellId);
        diseaseCell.setDateInsecticideApplication(date);
        diseaseCell.setPercentageOfCropCoverage(percentageOfCropCoverage);
    }

    @Override
    public void setupLayer() {}

    @Override
    public void executeLayer() {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public void executeLayer(LayerExecutionParams params) {
        LayerFunctionParams params1 = (LayerFunctionParams) params;
        if(this.currentDateLayerExecution == null) {
            this.iterateGraphForDate(params1.getDate());
            this.currentDateLayerExecution = params1.getDate();
            logger.info("For Date: "+params1.getDate());
            this.printCellsStates();
        } else {
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern(this.worldConfig.getProperty("date.format"));
            int daysBetweenLastDataAndNewEvent = DateHelper.differenceDaysBetweenTwoDates(this.currentDateLayerExecution,params1.getDate());
            for (int i = 0; i < daysBetweenLastDataAndNewEvent; i++) {
                DateTime previousStateDate = DateHelper.getDateInJoda(this.currentDateLayerExecution);
                DateTime previousStateDatePlusOneDay = previousStateDate.plusDays(1);
                String newDate = dtfOut.print(previousStateDatePlusOneDay);
                this.iterateGraphForDate(newDate);
                this.currentDateLayerExecution = newDate;
                logger.info("-----------> For Date: "+newDate+" <-----------");
                this.printCellsStates();
            }
        }
    }

    /**
     * Given a date iterates all the cells and its neighbors executing the transition rules for diseases
     * @param dateExecution date for the layer execution
     */
    private void iterateGraphForDate(String dateExecution) {
        for(DiseaseCell currentCell: this.allCells) {
            DiseaseCellState currentCellState = (DiseaseCellState) currentCell.getCellState();
            double probabilityDiseaseConfigured = Double.parseDouble(this.worldConfig.getProperty("disease.incrementProbabilityPerDay"));
            //If first layer execution just creates the first state with the current date, for this case the probability of each cell always starts at 0
            if(currentCell.getCellState() == null) {
                DiseaseCellState newCellState = new DiseaseCellState();
                newCellState.setCurrentProbabilityDisease(probabilityDiseaseConfigured);
                newCellState.setInfected(false);
                currentCell.setCellState(dateExecution, newCellState);
            } else {
                double insecticideDaysEffectiveness = Integer.parseInt(this.worldConfig.getProperty("disease.insecticideEfficacyDays"));
                double incrementNeighborInfected = Double.parseDouble(this.worldConfig.getProperty("disease.incrementNeighborInfected"));
                int quantityNeighborsInfected = 0;
                // If no first layer execution, then reviews current state if peasant added insecticide and neighbor cells, if not then runs the configured rule to increase the probability
                List<DiseaseCell> neighbors = Graphs.neighborListOf(this.simpleCellGraph, currentCell);
                for(DiseaseCell neighbor: neighbors) {
                    if(((DiseaseCellState)neighbor.getCellState()).isInfected()) {
                        quantityNeighborsInfected ++;
                    }
                }
                // Verifies if the crop disease cell doesn't have insecticide or the date after application is bigger than the configured
                DiseaseCellState newCellState = new DiseaseCellState();
                if(this.worldConfig.isDiseasePerturbation()) {
                    double nextRand = this.random.nextDouble();
                    logger.info("Current rand for disease "+nextRand);
                    this.updateCellInsecticideFromCellEvents(currentCell);
                    if(currentCell.getDateInsecticideApplication() == null || DateHelper.differenceDaysBetweenTwoDates(dateExecution,currentCell.getDateInsecticideApplication())>insecticideDaysEffectiveness) {
                        newCellState.setCurrentProbabilityDisease(currentCellState.getCurrentProbabilityDisease() + probabilityDiseaseConfigured);
                        // Evaluates if should get infected if the random is less or equal of the current accumulated probability of generating a disease plus the quantity of neighbors infected multiplied the configured factor
                        newCellState.setInfected(((DiseaseCellState) currentCell.getCellState()).isInfected() || nextRand <= currentCellState.getCurrentProbabilityDisease() + quantityNeighborsInfected * incrementNeighborInfected);
                    } else {
                        double insecticideCoverage = currentCell.getPercentageOfCropCoverage();
                        newCellState.setCurrentProbabilityDisease(0);
                        newCellState.setInfected(false);
                    }
                } else {
                    newCellState.setCurrentProbabilityDisease(0);
                    newCellState.setInfected(false);
                }
                currentCell.setCellState(dateExecution, newCellState);
            }
        }
    }

    private void updateCellInsecticideFromCellEvents(DiseaseCell diseaseCell) {
        if(diseaseCell.getCellActions().size() > 0) {
            DiseaseCellAction cellAction = (DiseaseCellAction) diseaseCell.getCellActions().get(0);
            diseaseCell.setDateInsecticideApplication(cellAction.getDate());
            diseaseCell.setPercentageOfCropCoverage(Double.parseDouble(cellAction.getPayload()));
            diseaseCell.setCellActions(new ArrayList<>());
        }
    }

    /**
     * Prints the cell states
     */
    private void printCellsStates() {
        for(DiseaseCell currentCell: this.allCells) {
            logger.info("Cell: "+currentCell.getId());
            logger.info("infected: "+((DiseaseCellState)currentCell.getCellState()).isInfected());
            logger.info("current probability of infection: "+((DiseaseCellState)currentCell.getCellState()).getCurrentProbabilityDisease());
            logger.info("-------------------");
        }
    }

    /**
     * Adds a disease action in this case insecticide, will affect the calculation during the layer execution
     * @param diseaseCellId Id of the cell
     * @param groundCoverage percentage of coverage of the crop
     * @param date date of application
     */
    public void addInsecticideEvent(String diseaseCellId, String groundCoverage, String date) {
        DiseaseCellAction diseaseCellAction = new DiseaseCellAction(groundCoverage,date);
        this.cellDirectory.get(diseaseCellId).addCellAction(diseaseCellAction);
    }
}
