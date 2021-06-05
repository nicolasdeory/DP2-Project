package acme.features.administrator.dashboard;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.forms.Dashboard;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractShowService;
import acme.utils.WorkLoadOperations;

@Service
public class AdministratorDashboardShowService implements AbstractShowService<Administrator, Dashboard> {
	
	// Internal state ---------------------------------------------------------

		@Autowired
		protected AdministratorDashboardRepository repository;
		
		@Autowired
		protected AdministratorDashboardService service;

		// AbstractShowService<Administrator, Dashboard> interface ----------------
	@Override
	public boolean authorise(final Request<Dashboard> request) {
		AssertUtils.assertRequestNotNull(request);
		return true;
	}

	@Override
	public void unbind(final Request<Dashboard> request, final Dashboard entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);
		
		
		request.unbind(entity, model, "numberOfPublicTasks","numberOfPrivateTasks","numberOfFinishedTasks","numberOfNonFinishedTasks","numberOfPublicWorkPlans","numberOfPrivateWorkPlans",
			"numberOfFinishedWorkPlans","numberOfNonFinishedWorkPlans","numberOfWorkPlans","averageOfTaskExecutionPeriods","deviationOfTaskExecutionPeriods","minOfTaskExecutionPeriods","maxOfTaskExecutionPeriods",
			"averageOfWorkPlanExecutionPeriods","deviationOfWorkPlanExecutionPeriods","minOfWorkPlanExecutionPeriods","maxOfWorkPlanExecutionPeriods","averageOfTaskWorkloads","deviationOfTaskWorkloads",
			"minOfTaskWorkloads","maxOfTaskWorkloads","averageOfWorkplanWorkloads","deviationOfWorkplanWorkloads","minOfWorkplanWorkloads","maxOfWorkplanWorkloads",
				"XXXFlaggedAsXXX","shoutXXXRateInXXX","XXX_XXCurrencyAverage","XXX_XXCurrencyDeviation","XXX_YYCurrencyAverage","XXX_YYCurrencyDeviationYY");
	}

	@Override
	public Dashboard findOne(final Request<Dashboard> request) {
		AssertUtils.assertRequestNotNull(request);
		
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
		
		Double averageOfWorkPlanExecutionPeriods;
		Double deviationOfWorkPlanExecutionPeriods;
		Double minOfWorkPlanExecutionPeriods;
		Double maxOfWorkPlanExecutionPeriods;
		
		Double averageOfWorkplanWorkloads;
		Double deviationOfWorkplanWorkloads;
		Double minOfWorkplanWorkloads;
		Double maxOfWorkplanWorkloads;

		Double XXXFlaggedAsXXX;
		Double shoutXXXRateInXXX;
		Double XXX_XXCurrencyAverage;
		Double XXX_XXCurrencyDeviation;
		Double XXX_YYCurrencyAverage;
		Double XXX_YYCurrencyDeviationYY;
		
		numberOfPublicTasks = this.repository.numberOfPublicTasks();
		numberOfPrivateTasks = this.repository.numberOfPrivateTasks();
		numberOfFinishedTasks = this.repository.numberOfFinishedTasks();
		numberOfNonFinishedTasks = this.repository.numberOfNonFinishedTasks();
		
		averageOfTaskExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.getAllTasks().stream().mapToDouble(x->x.getExecutionPeriod().getWorkloadHours()).average().getAsDouble());
		deviationOfTaskExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.deviationOfTaskExecutionPeriods());
		minOfTaskExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.minOfTaskExecutionPeriods());
		maxOfTaskExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.maxOfTaskExecutionPeriods());
		
		averageOfTaskWorkloads = this.service.averageOfTaskWorkload();
		deviationOfTaskWorkloads = this.service.deviationOfTaskWorkload();
		minOfTaskWorkloads = this.repository.getAllTasks().stream().mapToDouble(x->x.getExecutionPeriod().getWorkloadHours()).min().getAsDouble();
		maxOfTaskWorkloads = this.repository.getAllTasks().stream().mapToDouble(x->x.getExecutionPeriod().getWorkloadHours()).max().getAsDouble();
		
		numberOfPublicWorkPlans = this.repository.numberOfPublicWorkPlans();
		numberOfPrivateWorkPlans = this.repository.numberOfPrivateWorkPlans();
		numberOfFinishedWorkPlans = this.repository.numberOfFinishedWorkPlans();
		numberOfNonFinishedWorkPlans = this.repository.numberOfNonFinishedWorkPlans();
		numberOfWorkPlans = this.repository.numberOfWorkPlans();
		
		averageOfWorkPlanExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.averageOfWorkPlanExecutionPeriods());
		deviationOfWorkPlanExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.deviationOfWorkPlanExecutionPeriods());
		minOfWorkPlanExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.minOfWorkPlanExecutionPeriods());
		maxOfWorkPlanExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.maxOfWorkPlanExecutionPeriods());
		
		averageOfWorkplanWorkloads = this.service.averageOfWorkPlanWorkload();
		deviationOfWorkplanWorkloads = this.service.deviationOfWorkPlanWorkload();
		minOfWorkplanWorkloads = this.repository.findAllWorkPlans().stream().mapToDouble(x->x.getExecutionPeriod().getWorkloadHours()).min().getAsDouble();
		maxOfWorkplanWorkloads = this.repository.findAllWorkPlans().stream().mapToDouble(x->x.getExecutionPeriod().getWorkloadHours()).max().getAsDouble();

		XXXFlaggedAsXXX=this.repository.getXXXFlaggedAsXXX();
		shoutXXXRateInXXX=this.service.getShoutXXXRateInXXX();
		XXX_XXCurrencyAverage=this.repository.getXXX_XXCurrencyAverage();
		XXX_XXCurrencyDeviation=this.repository.getXXX_XXCurrencyDeviation();
		XXX_YYCurrencyAverage=this.repository.getXXX_YYCurrencyAverage();
		XXX_YYCurrencyDeviationYY=this.repository.getXXX_YYCurrencyDeviationYY();
		
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
		
		result.setAverageOfWorkPlanExecutionPeriods(averageOfWorkPlanExecutionPeriods);
		result.setDeviationOfWorkPlanExecutionPeriods(deviationOfWorkPlanExecutionPeriods);
		result.setMinOfWorkPlanExecutionPeriods(minOfWorkPlanExecutionPeriods);
		result.setMaxOfWorkPlanExecutionPeriods(maxOfWorkPlanExecutionPeriods);
		
		result.setAverageOfWorkplanWorkloads(averageOfWorkplanWorkloads);
		result.setDeviationOfWorkplanWorkloads(deviationOfWorkplanWorkloads);
		result.setMinOfWorkplanWorkloads(minOfWorkplanWorkloads);
		result.setMaxOfWorkplanWorkloads(maxOfWorkplanWorkloads);

		result.setXXXFlaggedAsXXX(XXXFlaggedAsXXX);
		result.setShoutXXXRateInXXX(shoutXXXRateInXXX);
		result.setXXX_XXCurrencyAverage(XXX_XXCurrencyAverage);
		result.setXXX_XXCurrencyDeviation(XXX_XXCurrencyDeviation);
		result.setXXX_YYCurrencyAverage(XXX_YYCurrencyAverage);
		result.setXXX_YYCurrencyDeviationYY(XXX_YYCurrencyDeviationYY);


		return result;
	}

}
