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

		request.unbind(entity, model, "numberOfPublicTasks", "numberOfPrivateTasks", "numberOfFinishedTasks",
				"numberOfNonFinishedTasks", "numberOfPublicWorkPlans", "numberOfPrivateWorkPlans",
				"numberOfFinishedWorkPlans", "numberOfNonFinishedWorkPlans", "numberOfWorkPlans",
				"averageOfTaskExecutionPeriods", "deviationOfTaskExecutionPeriods", "minOfTaskExecutionPeriods",
				"maxOfTaskExecutionPeriods", "averageOfWorkPlanExecutionPeriods", "deviationOfWorkPlanExecutionPeriods",
				"minOfWorkPlanExecutionPeriods", "maxOfWorkPlanExecutionPeriods", "averageOfTaskWorkloads",
				"deviationOfTaskWorkloads", "minOfTaskWorkloads", "maxOfTaskWorkloads", "averageOfWorkplanWorkloads",
				"deviationOfWorkplanWorkloads", "minOfWorkplanWorkloads", "maxOfWorkplanWorkloads",
				"quartFlaggedAsImportant", "shoutsZeroBugdetRate",
				"quart_EURBudgetAverage", "quart_EURBudgetDeviation",
				"quart_DollarBudgetAverage",
				"quart_DollarBudgetDeviation",
				"quart_GBPBudgetAverage",
				"quart_GBPBudgetDeviation");
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

		Double quartFlaggedAsImportant;
		Double shoutsZeroBugdetRate;
		Double quart_EURBudgetAverage;
		Double quart_EURBudgetDeviation;
		Double quart_DollarBudgetAverage;
		Double quart_DollarBudgetDeviation;
		Double quart_GBPBudgetAverage;
		Double quart_GBPBudgetDeviation;

		numberOfPublicTasks = this.repository.numberOfPublicTasks();
		numberOfPrivateTasks = this.repository.numberOfPrivateTasks();
		numberOfFinishedTasks = this.repository.numberOfFinishedTasks();
		numberOfNonFinishedTasks = this.repository.numberOfNonFinishedTasks();

		averageOfTaskExecutionPeriods = WorkLoadOperations.formatWorkload(this.repository.getAllTasks().stream()
				.mapToDouble(x -> x.getExecutionPeriod().getWorkloadHours()).average().orElse(0));
		deviationOfTaskExecutionPeriods = WorkLoadOperations
				.formatWorkload(this.repository.deviationOfTaskExecutionPeriods().orElse(-1.));
		minOfTaskExecutionPeriods = WorkLoadOperations
				.formatWorkload(this.repository.minOfTaskExecutionPeriods().orElse(-1.));
		maxOfTaskExecutionPeriods = WorkLoadOperations
				.formatWorkload(this.repository.maxOfTaskExecutionPeriods().orElse(-1.));

		averageOfTaskWorkloads = this.service.averageOfTaskWorkload();
		deviationOfTaskWorkloads = this.service.deviationOfTaskWorkload();
		minOfTaskWorkloads = this.repository.getAllTasks().stream()
				.mapToDouble(x -> x.getExecutionPeriod().getWorkloadHours()).min().orElse(-1);
		maxOfTaskWorkloads = this.repository.getAllTasks().stream()
				.mapToDouble(x -> x.getExecutionPeriod().getWorkloadHours()).max().orElse(-1);

		numberOfPublicWorkPlans = this.repository.numberOfPublicWorkPlans();
		numberOfPrivateWorkPlans = this.repository.numberOfPrivateWorkPlans();
		numberOfFinishedWorkPlans = this.repository.numberOfFinishedWorkPlans();
		numberOfNonFinishedWorkPlans = this.repository.numberOfNonFinishedWorkPlans();
		numberOfWorkPlans = this.repository.numberOfWorkPlans();

		averageOfWorkPlanExecutionPeriods = WorkLoadOperations
				.formatWorkload(this.repository.averageOfWorkPlanExecutionPeriods().orElse(-1.));
		deviationOfWorkPlanExecutionPeriods = WorkLoadOperations
				.formatWorkload(this.repository.deviationOfWorkPlanExecutionPeriods().orElse(-1.));
		minOfWorkPlanExecutionPeriods = WorkLoadOperations
				.formatWorkload(this.repository.minOfWorkPlanExecutionPeriods().orElse(-1.));
		maxOfWorkPlanExecutionPeriods = WorkLoadOperations
				.formatWorkload(this.repository.maxOfWorkPlanExecutionPeriods().orElse(-1.));

		averageOfWorkplanWorkloads = this.service.averageOfWorkPlanWorkload();
		deviationOfWorkplanWorkloads = this.service.deviationOfWorkPlanWorkload();
		minOfWorkplanWorkloads = this.repository.findAllWorkPlans().stream()
				.mapToDouble(x -> x.getExecutionPeriod().getWorkloadHours()).min().orElse(-1);
		maxOfWorkplanWorkloads = this.repository.findAllWorkPlans().stream()
				.mapToDouble(x -> x.getExecutionPeriod().getWorkloadHours()).max().orElse(-1);

		quartFlaggedAsImportant = this.repository
				.getQuartsFlaggedAsImportant();
		shoutsZeroBugdetRate = this.service.getShoutsZeroBugdetRate();
		// -1 when no data persisted
		quart_EURBudgetAverage = this.repository
				.getQuart_EurBudgetAverage().orElse(-1.);
		quart_EURBudgetDeviation = this.repository
				.getQuart_EurBudgetDeviation().orElse(-1.);
		quart_DollarBudgetAverage = this.repository
				.getQuart_DollarBudgetAverage().orElse(-1.);
		quart_DollarBudgetDeviation = this.repository
				.getQuart_DollarBudgetDeviation().orElse(-1.);
		quart_GBPBudgetAverage = this.repository
				.getQuart_GBPBudgetAverage().orElse(-1.);
		quart_GBPBudgetDeviation = this.repository
				.getQuart_GBPBudgetDeviation().orElse(-1.);

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

		result.setQuartFlaggedAsImportant(quartFlaggedAsImportant);
		result.setShoutsZeroBugdetRate(shoutsZeroBugdetRate);
		result.setQuart_EURBudgetAverage(quart_EURBudgetAverage);
		result.setQuart_EURBudgetDeviation(quart_EURBudgetDeviation);
		result.setQuart_DollarBudgetAverage(
				quart_DollarBudgetAverage);
		result.setQuart_DollarBudgetDeviation(
				quart_DollarBudgetDeviation);
		result.setQuart_GBPBudgetAverage(
				quart_GBPBudgetAverage);
		result.setQuart_GBPBudgetDeviation(
				quart_GBPBudgetDeviation);

		return result;
	}

}
