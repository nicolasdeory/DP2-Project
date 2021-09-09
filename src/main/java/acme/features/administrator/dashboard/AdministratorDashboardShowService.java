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
				"nftFlaggedAsIgnoreNft", "shoutsZeroBugdetRate",
				"nft_EURSalaryAverage", "nft_EURSalaryDeviation",
				"nft_DollarSalaryAverage",
				"nft_DollarSalaryDeviation",
				"nft_GBPSalaryAverage",
				"nft_GBPSalaryDeviation");
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

		Double nftFlaggedAsIgnoreNft;
		Double shoutsZeroBugdetRate;
		Double nft_EURSalaryAverage;
		Double nft_EURSalaryDeviation;
		Double nft_DollarSalaryAverage;
		Double nft_DollarSalaryDeviation;
		Double nft_GBPSalaryAverage;
		Double nft_GBPSalaryDeviation;

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

		nftFlaggedAsIgnoreNft = this.repository
				.getNftsFlaggedAsIgnoreNft();
		shoutsZeroBugdetRate = this.service.getShoutsZeroBugdetRate();
		// -1 when no data persisted
		nft_EURSalaryAverage = this.repository
				.getNft_EurSalaryAverage().orElse(-1.);
		nft_EURSalaryDeviation = this.repository
				.getNft_EurSalaryDeviation().orElse(-1.);
		nft_DollarSalaryAverage = this.repository
				.getNft_DollarSalaryAverage().orElse(-1.);
		nft_DollarSalaryDeviation = this.repository
				.getNft_DollarSalaryDeviation().orElse(-1.);
		nft_GBPSalaryAverage = this.repository
				.getNft_GBPSalaryAverage().orElse(-1.);
		nft_GBPSalaryDeviation = this.repository
				.getNft_GBPSalaryDeviation().orElse(-1.);

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

		result.setNftFlaggedAsIgnoreNft(nftFlaggedAsIgnoreNft);
		result.setShoutsZeroBugdetRate(shoutsZeroBugdetRate);
		result.setNft_EURSalaryAverage(nft_EURSalaryAverage);
		result.setNft_EURSalaryDeviation(nft_EURSalaryDeviation);
		result.setNft_DollarSalaryAverage(
				nft_DollarSalaryAverage);
		result.setNft_DollarSalaryDeviation(
				nft_DollarSalaryDeviation);
		result.setNft_GBPSalaryAverage(
				nft_GBPSalaryAverage);
		result.setNft_GBPSalaryDeviation(
				nft_GBPSalaryDeviation);

		return result;
	}

}
