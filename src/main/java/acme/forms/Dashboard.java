package acme.forms;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dashboard implements Serializable {

	// Serialisation identifier -----------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------
	Integer numberOfPublicTasks;
	Integer numberOfPrivateTasks;
	Integer numberOfFinishedTasks;
	Integer numberOfNonFinishedTasks;

	Double averageOfTaskExecutionPeriods;
	Double deviationOfTaskExecutionPeriods;
	Double minOfTaskExecutionPeriods;
	Double maxOfTaskExecutionPeriods;

	Double averageOfTaskWorkloads;
	Double deviationOfTaskWorkloads;
	Double minOfTaskWorkloads;
	Double maxOfTaskWorkloads;

	Integer numberOfPublicWorkPlans;
	Integer numberOfPrivateWorkPlans;
	Integer numberOfFinishedWorkPlans;
	Integer numberOfNonFinishedWorkPlans;
	Integer numberOfWorkPlans;
	Double averageOfWorkPlanExecutionPeriods;
	Double deviationOfWorkPlanExecutionPeriods;
	Double minOfWorkPlanExecutionPeriods;
	Double maxOfWorkPlanExecutionPeriods;

	Double averageOfWorkplanWorkloads;
	Double deviationOfWorkplanWorkloads;
	Double minOfWorkplanWorkloads;
	Double maxOfWorkplanWorkloads;

	Double entityToChangeFlaggedAsFlagAttributeToChange;
	Double shoutsZeroBugdetRate;
	Double entityToChange_EURMoneyAttributeToChangeAverage;
	Double entityToChange_EURMoneyAttributeToChangeDeviation;
	Double entityToChange_DollarMoneyAttributeToChangeAverage;
	Double entityToChange_DollarMoneyAttributeToChangeDeviation;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
