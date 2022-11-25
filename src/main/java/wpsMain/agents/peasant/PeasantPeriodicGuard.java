package wpsMain.agents.peasant;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import wpsMain.DateSingleton;
import wpsMain.agents.messages.peasant.PeasantMessage;
import wpsMain.agents.messages.peasant.PeasantMessageType;
import wpsMain.util.WorldConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wpsMain.world.helper.DateHelper;
import org.joda.time.DateTime;

import java.util.Random;

/**
 * BESA peasant's periodic guard, wakes up the peasant, then he reacts if he will check the crop
 */
public class PeasantPeriodicGuard extends PeriodicGuardBESA {
    private Random random = new Random();
    private static final Logger logger = LogManager.getLogger(PeasantPeriodicGuard.class);

    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        WorldConfiguration worldConfiguration = WorldConfiguration.getPropsInstance();
        PeasantState peasantState = (PeasantState) this.agent.getState();
        DateSingleton singleton = DateSingleton.getInstance();
        String currentDate = singleton.getCurrentDate();
        logger.info("Current date: "+currentDate);
        DateTime dateTime = DateHelper.getDateInJoda(currentDate);
        if(worldConfiguration.isCoursePerturbation() && !peasantState.getMonthsTakenCourse().contains(dateTime.getMonthOfYear())){
            //Verifies if the simulation has the course perturbation, if so then changes the peasant probabilities for that month
            double newWaterCropProbability = peasantState.getProbabilityOfWaterCropIfWaterStress() + Double.parseDouble(worldConfiguration.getProperty("course.waterCropIfStressImprovement"));
            double newInsecticideProbability = peasantState.getProbabilityOfPesticideIfDisease() + Double.parseDouble(worldConfiguration.getProperty("course.insecticideIfDiseaseImprovement"));
            peasantState.setProbabilityOfWaterCropIfWaterStress(newWaterCropProbability > 1 ? 1 : newWaterCropProbability);
            peasantState.setProbabilityOfPesticideIfDisease(newInsecticideProbability > 1 ? 1 : newInsecticideProbability);
            peasantState.getMonthsTakenCourse().add(dateTime.getMonthOfYear());
        }
        // Evaluate if the peasant will check the crop based on his probability
        if(this.random.nextDouble() <= peasantState.getProbabilityOfDailyCropSupervision()) {
            try {
                AgHandlerBESA ah = this.agent.getAdmLocal().getHandlerByAid(this.agent.getAid());
                PeasantMessage peasantMessageRequestCropInformation = new PeasantMessage(PeasantMessageType.REQUEST_CROP_INFORMATION,ah.getAgId(), null);
                peasantMessageRequestCropInformation.setDate(currentDate);
                singleton.getDatePlusOneDayAndUpdate();
                EventBESA eventBESASend = new EventBESA(PeasantGuard.class.getName(),peasantMessageRequestCropInformation);
                ah.sendEvent(eventBESASend);
            }catch (ExceptionBESA exceptionBESA) {
                exceptionBESA.printStackTrace();
            }
        } else {
            singleton.getDatePlusOneDayAndUpdate();
        }
    }
}
