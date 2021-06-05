package acme.features.administrator.dashboard;

import java.util.List;

import acme.entities.shouts.Shout;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tasks.Task;
import acme.entities.workplan.WorkPlan;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository{

	@Query("select t.workload from Task t")
	List<Double> findAllTaskWorkload();
	
	@Query("select w from WorkPlan w")
	List<WorkPlan> findAllWorkPlans();
	
	@Query("select count(t) from Task t where t.isPublic=True")
	Integer numberOfPublicTasks();
	
	@Query("select count(t) from Task t where t.isPublic=False")
	Integer numberOfPrivateTasks();
	
	@Query("select count(t) from Task t where t.executionPeriod.finishDateTime < CURRENT_TIMESTAMP")
	Integer numberOfFinishedTasks();

	@Query("select count(t) from Task t where t.executionPeriod.finishDateTime > CURRENT_TIMESTAMP")
	Integer numberOfNonFinishedTasks();
	
	@Query("select t from Task t")
	List<Task> getAllTasks();

	@Query("select s from Shout s")
	List<Shout> getAllShouts();
	
	@Query("select (stddev((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(60000*60) from Task t")
	Double deviationOfTaskExecutionPeriods();
	
	@Query("select (min((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(60000*60) from Task t")
	Double minOfTaskExecutionPeriods();
	
	@Query("select (max((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(60000*60) from Task t")
	Double maxOfTaskExecutionPeriods();
	
	@Query("select (avg((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Double averageOfTaskWorkloads();

	@Query("select (stddev((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Double deviationOfTaskWorkloads();
	
	@Query("select (min((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Double minOfTaskWorkloads();
	
	@Query("select (max((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Double maxOfTaskWorkloads();

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
	
	@Query("select (avg((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(60000*60) from WorkPlan w")
	Double averageOfWorkPlanExecutionPeriods();
	
	@Query("select (stddev((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(60000*60) from WorkPlan w")
	Double deviationOfWorkPlanExecutionPeriods();
	
	@Query("select (min((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(60000*60) from WorkPlan w")
	Double minOfWorkPlanExecutionPeriods();
	
	@Query("select (max((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(60000*60) from WorkPlan w")
	Double maxOfWorkPlanExecutionPeriods();
	
	@Query("select (avg((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Double averageOfWorkplanWorkloads();
	
	@Query("select (stddev((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Double deviationOfWorkplanWorkloads();
	
	@Query("select (min((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Double minOfWorkplanWorkloads();
	
	@Query("select (max((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Double maxOfWorkplanWorkloads();

	@Query("select count (x) from XXX x where x.XXXflag=TRUE")
	Double getXXXFlaggedAsXXX();
	@Query("select avg(x.currency.amount) from XXX x where x.currency.currency= \"XX\" ")
	Double getXXX_XXCurrencyAverage();
	@Query("select stddev(x.currency.amount) from XXX x where x.currency.currency= \"XX\" ")
	Double getXXX_XXCurrencyDeviation();
	@Query("select avg(x.currency.amount) from XXX x where x.currency.currency= \"YY\" ")
	Double getXXX_YYCurrencyAverage();
	@Query("select stddev(x.currency.amount) from XXX x where x.currency.currency= \"YY\" ")
	Double getXXX_YYCurrencyDeviationYY();

	/////////////////////////////////////////////////
	
	/////////////////////////////////////////////////
	
}
