package acme.features.administrator.dashboard;

import java.util.List;
import java.util.Optional;

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
	Optional<Double> deviationOfTaskExecutionPeriods();
	
	@Query("select (min((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(60000*60) from Task t")
	Optional<Double> minOfTaskExecutionPeriods();
	
	@Query("select (max((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(60000*60) from Task t")
	Optional<Double> maxOfTaskExecutionPeriods();
	
	@Query("select (avg((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Optional<Double> averageOfTaskWorkloads();

	@Query("select (stddev((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Optional<Double> deviationOfTaskWorkloads();
	
	@Query("select (min((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Optional<Double> minOfTaskWorkloads();
	
	@Query("select (max((t.executionPeriod.finishDateTime)-(t.executionPeriod.startDateTime)))/(1000*3600.0) from Task t")
	Optional<Double> maxOfTaskWorkloads();

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
	Optional<Double> averageOfWorkPlanExecutionPeriods();
	
	@Query("select (stddev((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(60000*60) from WorkPlan w")
	Optional<Double> deviationOfWorkPlanExecutionPeriods();
	
	@Query("select (min((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(60000*60) from WorkPlan w")
	Optional<Double> minOfWorkPlanExecutionPeriods();
	
	@Query("select (max((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(60000*60) from WorkPlan w")
	Optional<Double> maxOfWorkPlanExecutionPeriods();
	
	@Query("select (avg((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Optional<Double> averageOfWorkplanWorkloads();
	
	@Query("select (stddev((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Optional<Double> deviationOfWorkplanWorkloads();
	
	@Query("select (min((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Optional<Double> minOfWorkplanWorkloads();
	
	@Query("select (max((w.executionPeriod.finishDateTime)-(w.executionPeriod.startDateTime)))/(1000*3600.0) from WorkPlan w")
	Optional<Double> maxOfWorkplanWorkloads();

	@Query("select count (x) from Iowe x where x.important=TRUE")
	Double getIowesFlaggedAsImportant();

	@Query("select avg(x.budget.amount) from Iowe x where x.budget.currency= 'EUR' ")
	Optional<Double> getIowe_EURBudgetAverage();
	@Query("select stddev(x.budget.amount) from Iowe x where x.budget.currency= 'EUR'  ")
	Optional<Double> getIowe_EURBudgetDeviation();
	@Query("select avg(x.budget.amount) from Iowe x where x.budget.currency= 'USD' ")
	Optional<Double> getIowe_USDCurrencyAverage();
	@Query("select stddev(x.budget.amount) from Iowe x where x.budget.currency= 'USD' ")
	Optional<Double> getIowe_USDCurrencyDeviation();

	/////////////////////////////////////////////////
	
	/////////////////////////////////////////////////
	
}
