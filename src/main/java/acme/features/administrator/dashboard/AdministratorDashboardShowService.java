package acme.features.administrator.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.forms.Dashboard;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractShowService;

@Service
public class AdministratorDashboardShowService implements AbstractShowService<Administrator, Dashboard> {
	
	// Internal state ---------------------------------------------------------

		@Autowired
		protected AdministratorDashboardRepository repository;

		// AbstractShowService<Administrator, Dashboard> interface ----------------
	@Override
	public boolean authorise(final Request<Dashboard> request) {
		assert request != null;
		return true;
	}

	@Override
	public void unbind(final Request<Dashboard> request, final Dashboard entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
//		request.unbind(entity, model,"numberOfPublicTasks","numberOfPrivateTasks","numberOfFinishedTasks","numberOfNonFinishedTasks",
//			"averageOfTaskExecutionPeriods","deviationOfTaskExecutionPeriod","minOfTaskExecutionPeriods"," maxOfTaskExecutionPeriods",
//			"averageOfTaskWorkloads","deviationOfTaskWorkloads"," minOfTaskWorkloads","maxOfTaskWorkloads","numberOfPublicWorkPlans",
//			"numberOfPrivateWorkPlans","numberOfFinishedWorkPlans","numberOfNonFinishedWorkPlans","averageOfWorkPlanPeriods",
//			"deviationOfWorkPlanPeriods","minOfWorkPlanPeriods","maxOfWorkPlanPeriods","averageOfWorkplanWorkloads","deviationOfWorkplanWorkloads",
//			" minOfWorkplanWorkloads","maxOfWorkplanWorkloads","numberOfWorkPlans","numberOfPublishedWorkPlans","numberOfNonPublishedWorkPlans");
		
		request.unbind(entity, model, "numberOfPublicTasks","numberOfPrivateTasks","numberOfFinishedTasks","numberOfNonFinishedTasks");
	}

	@Override
	public Dashboard findOne(final Request<Dashboard> request) {
		assert request != null;
		
		Dashboard result;
		Integer numberOfPublicTasks;
		Integer numberOfPrivateTasks;
		
		Integer numberOfFinishedTasks;
		Integer numberOfNonFinishedTasks;
//		
//		final Double averageOfTaskExecutionPeriods;
//		final Double deviationOfTaskExecutionPeriods;
//		final Integer minOfTaskExecutionPeriods;
//		final Integer maxOfTaskExecutionPeriods;
//		
//		final Double averageOfTaskWorkloads;
//		final Double deviationOfTaskWorkloads;
//		final Integer minOfTaskWorkloads;
//		final Integer maxOfTaskWorkloads;
//		
//		final Integer numberOfPublicWorkPlans;
//		final Integer numberOfPrivateWorkPlans;
//		
//		final Integer numberOfFinishedWorkPlans;
//		final Integer numberOfNonFinishedWorkPlans;
//		
//		final Double averageOfWorkPlanPeriods;
//		final Double deviationOfWorkPlanPeriods;
//		final Integer minOfWorkPlanPeriods;
//		final Integer maxOfWorkPlanPeriods;
//		
//		final Double averageOfWorkplanWorkloads;
//		final Double deviationOfWorkplanWorkloads;
//		final Integer minOfWorkplanWorkloads;
//		final Integer maxOfWorkplanWorkloads;
//		
//		final Integer numberOfWorkPlans;
//		final Integer numberOfPublishedWorkPlans;
//		final Integer numberOfNonPublishedWorkPlans;
		
		numberOfPublicTasks = this.repository.numberOfPublicTasks();
		numberOfPrivateTasks = this.repository.numberOfPrivateTasks();
		numberOfFinishedTasks = this.repository.numberOfFinishedTasks();
		numberOfNonFinishedTasks = this.repository.numberOfNonFinishedTasks();
//		averageOfTaskExecutionPeriods = this.repository.averageOfTaskExecutionPeriods();
//		deviationOfTaskExecutionPeriods = this.repository.deviationOfTaskExecutionPeriods();
//		minOfTaskExecutionPeriods = this.repository.minOfTaskExecutionPeriods();
//		maxOfTaskExecutionPeriods = this.repository.maxOfTaskExecutionPeriods();
//		averageOfTaskWorkloads = this.repository.averageOfTaskWorkloads();
//		deviationOfTaskWorkloads = this.repository.deviationOfTaskWorkloads();
//		minOfTaskWorkloads = this.repository.minOfTaskWorkloads();
//		maxOfTaskWorkloads = this.repository.maxOfTaskWorkloads();
//		numberOfPublicWorkPlans = this.repository.numberOfPublicWorkPlans();
//		numberOfPrivateWorkPlans = this.repository.numberOfPrivateWorkPlans();
//		numberOfFinishedWorkPlans = this.repository.numberOfFinishedWorkPlans();
//		numberOfNonFinishedWorkPlans = this.repository.numberOfNonFinishedWorkPlans();
//		averageOfWorkPlanPeriods = this.repository.averageOfWorkPlanPeriods();
//		deviationOfWorkPlanPeriods = this.repository.deviationOfWorkPlanPeriods();
//		minOfWorkPlanPeriods = this.repository.minOfWorkPlanPeriods();
//		maxOfWorkPlanPeriods = this.repository.maxOfWorkPlanPeriods();
//		averageOfWorkplanWorkloads = this.repository.averageOfWorkplanWorkloads();
//		deviationOfWorkplanWorkloads = this.repository.deviationOfWorkplanWorkloads();
//		minOfWorkplanWorkloads = this.repository.minOfWorkplanWorkloads();
//		maxOfWorkplanWorkloads = this.repository.maxOfWorkplanWorkloads();
//		numberOfWorkPlans = this.repository.numberOfWorkPlans();
//		numberOfPublishedWorkPlans = this.repository.numberOfPublishedWorkPlans();
//		numberOfNonPublishedWorkPlans = this.repository.numberOfNonPublishedWorkPlans();
		
		result = new Dashboard();
		result.setNumberOfPublicTasks(numberOfPublicTasks);
		result.setNumberOfPrivateTasks(numberOfPrivateTasks);
		result.setNumberOfFinishedTasks(numberOfFinishedTasks);
		result.setNumberOfNonFinishedTasks(numberOfNonFinishedTasks);
//		result.setAverageOfTaskExecutionPeriods(averageOfTaskExecutionPeriods);
//		result.setDeviationOfTaskExecutionPeriods(deviationOfTaskExecutionPeriods);
//		result.setMinOfTaskExecutionPeriods(minOfTaskExecutionPeriods);
//		result.setMaxOfTaskExecutionPeriods(maxOfTaskExecutionPeriods);
//		result.setAverageOfTaskWorkloads(averageOfTaskWorkloads);
//		result.setDeviationOfTaskWorkloads(deviationOfTaskWorkloads);
//		result.setMinOfTaskWorkloads(minOfTaskWorkloads);
//		result.setMaxOfTaskWorkloads(maxOfTaskWorkloads);
//		result.setNumberOfPublicWorkPlans(numberOfPublicWorkPlans);
//		result.setNumberOfPrivateWorkPlans(numberOfPrivateWorkPlans);
//		result.setNumberOfFinishedWorkPlans(numberOfFinishedWorkPlans);
//		result.setNumberOfNonFinishedWorkPlans(numberOfNonFinishedWorkPlans);
//		result.setAverageOfWorkPlanPeriods(averageOfWorkPlanPeriods);
//		result.setDeviationOfWorkPlanPeriods(deviationOfWorkPlanPeriods);
//		result.setMinOfWorkPlanPeriods(minOfWorkPlanPeriods);
//		result.setMaxOfWorkPlanPeriods(maxOfWorkPlanPeriods);
//		result.setAverageOfWorkplanWorkloads(averageOfWorkplanWorkloads);
//		result.setDeviationOfWorkplanWorkloads(deviationOfWorkplanWorkloads);
//		result.setMinOfWorkplanWorkloads(minOfWorkplanWorkloads);
//		result.setMaxOfWorkplanWorkloads(maxOfWorkplanWorkloads);
//		result.setNumberOfWorkPlans(numberOfWorkPlans);
//		result.setNumberOfPublishedWorkPlans(numberOfPublishedWorkPlans);
//		result.setNumberOfNonPublishedWorkPlans(numberOfNonPublishedWorkPlans);
		return result;
	}

}
