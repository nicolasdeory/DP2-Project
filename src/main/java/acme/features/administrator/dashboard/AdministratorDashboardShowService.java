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
		
		
		request.unbind(entity, model, "numberOfPublicTasks","numberOfPrivateTasks","numberOfFinishedTasks","numberOfNonFinishedTasks","numberOfPublicWorkPlans","numberOfPrivateWorkPlans",
			"numberOfFinishedWorkPlans","numberOfNonFinishedWorkPlans","numberOfWorkPlans","averageOfTaskExecutionPeriods","deviationOfTaskExecutionPeriods","minOfTaskExecutionPeriods","maxOfTaskExecutionPeriods",
			"averageOfWorkPlanExecutionPeriods","deviationOfWorkPlanExecutionPeriods","minOfWorkPlanExecutionPeriods","maxOfWorkPlanExecutionPeriods","averageOfTaskWorkloads","deviationOfTaskWorkloads",
			"minOfTaskWorkloads","maxOfTaskWorkloads","averageOfWorkplanWorkloads","deviationOfWorkplanWorkloads","minOfWorkplanWorkloads","maxOfWorkplanWorkloads");
	}

	@Override
	public Dashboard findOne(final Request<Dashboard> request) {
		assert request != null;
		
		Dashboard result;
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
//		Integer numberOfPublishedWorkPlans;
//		Integer numberOfNonPublishedWorkPlans;
		
		Double averageOfWorkPlanExecutionPeriods;
		Double deviationOfWorkPlanExecutionPeriods;
		Double minOfWorkPlanExecutionPeriods;
		Double maxOfWorkPlanExecutionPeriods;
		
		Double averageOfWorkplanWorkloads;
		Double deviationOfWorkplanWorkloads;
		Double minOfWorkplanWorkloads;
		Double maxOfWorkplanWorkloads;
		

		
		numberOfPublicTasks = this.repository.numberOfPublicTasks();
		numberOfPrivateTasks = this.repository.numberOfPrivateTasks();
		numberOfFinishedTasks = this.repository.numberOfFinishedTasks();
		numberOfNonFinishedTasks = this.repository.numberOfNonFinishedTasks();
		
		averageOfTaskExecutionPeriods = this.repository.averageOfTaskExecutionPeriods();
		deviationOfTaskExecutionPeriods = this.repository.deviationOfTaskExecutionPeriods();
		minOfTaskExecutionPeriods = this.repository.minOfTaskExecutionPeriods();
		maxOfTaskExecutionPeriods = this.repository.maxOfTaskExecutionPeriods();
		
		averageOfTaskWorkloads = this.repository.averageOfTaskWorkloads();
		deviationOfTaskWorkloads = this.repository.deviationOfTaskWorkloads();
		minOfTaskWorkloads = this.repository.minOfTaskWorkloads();
		maxOfTaskWorkloads = this.repository.maxOfTaskWorkloads();
		
		numberOfPublicWorkPlans = this.repository.numberOfPublicWorkPlans();
		numberOfPrivateWorkPlans = this.repository.numberOfPrivateWorkPlans();
		numberOfFinishedWorkPlans = this.repository.numberOfFinishedWorkPlans();
		numberOfNonFinishedWorkPlans = this.repository.numberOfNonFinishedWorkPlans();
		numberOfWorkPlans = this.repository.numberOfWorkPlans();
//		numberOfPublishedWorkPlans = this.repository.numberOfPublishedWorkPlans();
//		numberOfNonPublishedWorkPlans = this.repository.numberOfNonPublishedWorkPlans();
		
		averageOfWorkPlanExecutionPeriods = this.repository.averageOfWorkPlanExecutionPeriods();
		deviationOfWorkPlanExecutionPeriods = this.repository.deviationOfWorkPlanExecutionPeriods();
		minOfWorkPlanExecutionPeriods = this.repository.minOfWorkPlanExecutionPeriods();
		maxOfWorkPlanExecutionPeriods = this.repository.maxOfWorkPlanExecutionPeriods();
		
		averageOfWorkplanWorkloads = this.repository.averageOfWorkplanWorkloads();
		deviationOfWorkplanWorkloads = this.repository.deviationOfWorkplanWorkloads();
		minOfWorkplanWorkloads = this.repository.minOfWorkplanWorkloads();
		maxOfWorkplanWorkloads = this.repository.maxOfWorkplanWorkloads();

		
		result = new Dashboard();
		result.setNumberOfPublicTasks(numberOfPublicTasks);
		result.setNumberOfPrivateTasks(numberOfPrivateTasks);
		result.setNumberOfFinishedTasks(numberOfFinishedTasks);
		result.setNumberOfNonFinishedTasks(numberOfNonFinishedTasks);
		
		result.setAverageOfTaskExecutionPeriods(averageOfTaskExecutionPeriods);
		result.setDeviationOfTaskExecutionPeriods(deviationOfTaskExecutionPeriods);
		result.setMinOfTaskExecutionPeriods(minOfTaskExecutionPeriods);
		result.setMaxOfTaskExecutionPeriods(maxOfTaskExecutionPeriods);
		
		result.setAverageOfTaskWorkloads(averageOfTaskWorkloads);
		result.setDeviationOfTaskWorkloads(deviationOfTaskWorkloads);
		result.setMinOfTaskWorkloads(minOfTaskWorkloads);
		result.setMaxOfTaskWorkloads(maxOfTaskWorkloads);
		
		result.setNumberOfPublicWorkPlans(numberOfPublicWorkPlans);
		result.setNumberOfPrivateWorkPlans(numberOfPrivateWorkPlans);
		result.setNumberOfFinishedWorkPlans(numberOfFinishedWorkPlans);
		result.setNumberOfNonFinishedWorkPlans(numberOfNonFinishedWorkPlans);
		result.setNumberOfWorkPlans(numberOfWorkPlans);
//		result.setNumberOfPublishedWorkPlans(numberOfPublishedWorkPlans);
//		result.setNumberOfNonPublishedWorkPlans(numberOfNonPublishedWorkPlans);
		
		result.setAverageOfWorkPlanExecutionPeriods(averageOfWorkPlanExecutionPeriods);
		result.setDeviationOfWorkPlanExecutionPeriods(deviationOfWorkPlanExecutionPeriods);
		result.setMinOfWorkPlanExecutionPeriods(minOfWorkPlanExecutionPeriods);
		result.setMaxOfWorkPlanExecutionPeriods(maxOfWorkPlanExecutionPeriods);
		
		result.setAverageOfWorkplanWorkloads(averageOfWorkplanWorkloads);
		result.setDeviationOfWorkplanWorkloads(deviationOfWorkplanWorkloads);
		result.setMinOfWorkplanWorkloads(minOfWorkplanWorkloads);
		result.setMaxOfWorkplanWorkloads(maxOfWorkplanWorkloads);

		return result;
	}

}
