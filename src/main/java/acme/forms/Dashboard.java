package acme.forms;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dashboard implements Serializable {
	
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	Integer numberOfPublicTasks;
	Integer numberOfPrivateTasks;
	Integer numberOfFinishedTasks;
	Integer numberOfNonFinishedTasks;
//	
//	Double averageOfTaskExecutionPeriods;
//	Double deviationOfTaskExecutionPeriods;
//	Integer minOfTaskExecutionPeriods;
//	Integer maxOfTaskExecutionPeriods;
//	
//	Double averageOfTaskWorkloads;
//	Double deviationOfTaskWorkloads;
//	Integer minOfTaskWorkloads;
//	Integer maxOfTaskWorkloads;
//	
//	Integer numberOfPublicWorkPlans;
//	Integer numberOfPrivateWorkPlans;
//	
//	Integer numberOfFinishedWorkPlans;
//	Integer numberOfNonFinishedWorkPlans;
//	
//	Double averageOfWorkPlanPeriods;
//	Double deviationOfWorkPlanPeriods;
//	Integer minOfWorkPlanPeriods;
//	Integer maxOfWorkPlanPeriods;
//	
//	Double averageOfWorkplanWorkloads;
//	Double deviationOfWorkplanWorkloads;
//	Integer minOfWorkplanWorkloads;
//	Integer maxOfWorkplanWorkloads;
//	
//	Integer numberOfWorkPlans;
//	Integer numberOfPublishedWorkPlans;
//	Integer numberOfNonPublishedWorkPlans;

	

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
