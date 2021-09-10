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

	Double mockeFlaggedAsImportant;
	Double shoutsZeroBugdetRate;
	Double mocke_EURBudgetAverage;
	Double mocke_EURBudgetDeviation;
	Double mocke_DollarBudgetAverage;
	Double mocke_DollarBudgetDeviation;
	Double mocke_GBPBudgetAverage;
	Double mocke_GBPBudgetDeviation;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
