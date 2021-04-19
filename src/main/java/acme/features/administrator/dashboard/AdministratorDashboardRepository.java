package acme.features.administrator.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository{

	@Query("select count(t) from Task t where t.isPublic='True'")
	Integer numberOfPublicTasks();
	
	@Query("select count(t) from Task t where t.isPublic='False'")
	Integer numberOfPrivateTasks();
	
	@Query("select count(t) from Task t where t.executionPeriod.finishDateTime < CURRENT_TIMESTAMP")
	Integer numberOfFinishedTasks();

	@Query("select count(t) from Task t where t.executionPeriod.finishDateTime > CURRENT_TIMESTAMP")
	Integer numberOfNonFinishedTasks();
//	
//	@Query()
//	Double averageOfTaskExecutionPeriods();
//	
//	@Query()
//	Double deviationOfTaskExecutionPeriods();
//	
//	@Query()
//	Integer minOfTaskExecutionPeriods();
//	
//	@Query()
//	Integer maxOfTaskExecutionPeriods();
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
//	
//	@Query()
//	Integer numberOfPublicWorkPlans();
//	
//	@Query()
//	Integer numberOfPrivateWorkPlans();
//	
//	@Query()
//	Integer numberOfFinishedWorkPlans();
//	
//	@Query()
//	Integer numberOfNonFinishedWorkPlans();
//	
//	@Query()
//	Double averageOfWorkPlanPeriods();
//	
//	@Query()
//	Double deviationOfWorkPlanPeriods();
//	
//	@Query()
//	Integer minOfWorkPlanPeriods();
//	
//	@Query()
//	Integer maxOfWorkPlanPeriods();
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
//	
//	@Query()
//	Integer numberOfWorkPlans();
//	
//	@Query()
//	Integer numberOfPublishedWorkPlans();
//	
//	@Query()
//	Integer numberOfNonPublishedWorkPlans();
	
	
}
