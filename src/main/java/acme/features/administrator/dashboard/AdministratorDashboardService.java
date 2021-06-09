package acme.features.administrator.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acme.entities.shouts.Shout;
import acme.entities.workplan.WorkPlan;
import acme.utils.WorkLoadOperations;

@Service
public class AdministratorDashboardService {

	private final AdministratorDashboardRepository dashRepo;
	
	@Autowired
	public AdministratorDashboardService(final AdministratorDashboardRepository dashRepository){
		this.dashRepo=dashRepository;
	}
	
	@Transactional
	public Double averageOfTaskWorkload() {
		final List<Double> baseWorkloads = this.dashRepo.findAllTaskWorkload();
		final List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(baseWorkloads);
		return formattedWorkloads.stream().mapToDouble(a->a).average().orElse(-1);
	
		
		
	}
	
	@Transactional
	public Double deviationOfTaskWorkload() {
		final List<Double> baseWorkloads = this.dashRepo.findAllTaskWorkload();
		final List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(baseWorkloads);
		return AdministratorDashboardService.sd(formattedWorkloads);
		
	}
	
	@Transactional
	public Double averageOfWorkPlanWorkload() {
		final List<WorkPlan> baseWorkloads = this.dashRepo.findAllWorkPlans();
		final List<Double> workloads = baseWorkloads.stream().map(WorkPlan::getWorkloadHours).collect(Collectors.toList());
		final List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(workloads);
		return formattedWorkloads.stream().mapToDouble(a->a).average().orElse(-1);
		
	}
	
	@Transactional
	public Double deviationOfWorkPlanWorkload() {
		final List<WorkPlan> baseWorkloads = this.dashRepo.findAllWorkPlans();
		final List<Double> workloads = baseWorkloads.stream().map(WorkPlan::getWorkloadHours).collect(Collectors.toList());
		final List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(workloads);
		return AdministratorDashboardService.sd(formattedWorkloads);
	}
	
	public static List<Double> formatTime(final List<Double> baseWorkloads) {
		final List<Double> formattedWorkloads = new ArrayList<>();
		for (int i = 0; i < baseWorkloads.size();i++) {
			final Double workload = baseWorkloads.get(i);
			final Double newWorkload = WorkLoadOperations.unformatWorkload(workload);
			formattedWorkloads.add(newWorkload);
		}
		return formattedWorkloads;
	}

	
	public static Double sd (final List<Double> table)	{

		if (table.size() == 0)
			return -1.;
	    final double mean = table.stream().mapToDouble(a->a).average().orElse(-1);
	    double temp = 0;

	    for (int i = 0; i < table.size(); i++) {
	         final double val = table.get(i);
	         final double squrDiffToMean = Math.pow(val - mean, 2);
	        temp += squrDiffToMean;
	    }
	    final double meanOfDiffs = temp / (table.size());

	    return Math.sqrt(meanOfDiffs);
	}

	@Transactional
	public Double getShoutXXXRateInXXX(){
		final List<Shout> shouts=this.dashRepo.getAllShouts();
		if (shouts.isEmpty())
			return -1.;
		else
			return (double) shouts.stream().filter(x -> x.getPominok() != null && x.getPominok().getBudget() == null).count() /shouts.size();

	}
}
