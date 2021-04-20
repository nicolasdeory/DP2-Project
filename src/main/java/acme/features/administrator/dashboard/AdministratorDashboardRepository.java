package acme.features.administrator.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository{

	@Query("select count(t) from Task t where t.isPublic=True")
	Integer numberOfPublicTasks();
	
	@Query("select count(t) from Task t where t.isPublic=False")
	Integer numberOfPrivateTasks();
	
	@Query("select count(t) from Task t where t.executionPeriod.finishDateTime < CURRENT_TIMESTAMP")
	Integer numberOfFinishedTasks();

	@Query("select count(t) from Task t where t.executionPeriod.finishDateTime > CURRENT_TIMESTAMP")
	Integer numberOfNonFinishedTasks();
	
	@Query("select avg((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)) from Task t")
	Double averageOfTaskExecutionPeriods();
	
	@Query("select stddev((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)) from Task t")
	Double deviationOfTaskExecutionPeriods();
	
	@Query("select min((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime))from Task t")
	Double minOfTaskExecutionPeriods();
	
	@Query("select max((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)) from Task t")
	Double maxOfTaskExecutionPeriods();
//	
//	@Query()
//	Double averageOfTaskWorkloads();
//
//	@Query()
//	Double deviationOfTaskWorkloads();
//	
//	@Query()
//	Integer minOfTaskWorkloads();
//	
//	@Query()
//	Integer maxOfTaskWorkloads();

	@Query("select count(w) from WorkPlan w")
	Integer numberOfWorkPlans();
	
	@Query("select count(w) from WorkPlan w where w.isPublic=True")
	Integer numberOfPublicWorkPlans();
	
	@Query("select count(w) from WorkPlan w where w.isPublic=False")
	Integer numberOfPrivateWorkPlans();
	
	@Query("select count(w) from WorkPlan w where w.executionPeriod.finishDateTime < CURRENT_TIMESTAMP")
	Integer numberOfFinishedWorkPlans();
	
	@Query("select count(w) from WorkPlan w where w.executionPeriod.finishDateTime > CURRENT_TIMESTAMP")
	Integer numberOfNonFinishedWorkPlans();
	
//	@Query()
//	Integer numberOfPublishedWorkPlans();
//	
//	@Query()
//	Integer numberOfNonPublishedWorkPlans();
//	
	@Query("select avg((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)) from WorkPlan w")
	Double averageOfWorkPlanExecutionPeriods();
	
	@Query("select stddev((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)) from WorkPlan w")
	Double deviationOfWorkPlanExecutionPeriods();
	
	@Query("select min((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)) from WorkPlan w")
	Double minOfWorkPlanExecutionPeriods();
	
	@Query("select max((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)) from WorkPlan w")
	Double maxOfWorkPlanExecutionPeriods();
//	
//	@Query()
//	Double averageOfWorkplanWorkloads();
//	
//	@Query()
//	Double deviationOfWorkplanWorkloads();
//	
//	@Query()
//	Integer minOfWorkplanWorkloads();
//	
//	@Query()
//	Integer maxOfWorkplanWorkloads();

	
	
}
