package acme.features.administrator.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import acme.entities.shouts.Shout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		List<Double> baseWorkloads = this.dashRepo.findAllTaskWorkload();
		List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(baseWorkloads);
		return formattedWorkloads.stream().mapToDouble(a->a).average().getAsDouble();
	
		
		
	}
	
	@Transactional
	public Double deviationOfTaskWorkload() {
		List<Double> baseWorkloads = this.dashRepo.findAllTaskWorkload();
		List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(baseWorkloads);
		return AdministratorDashboardService.sd(formattedWorkloads);
		
	}
	
	@Transactional
	public Double averageOfWorkPlanWorkload() {
		List<WorkPlan> baseWorkloads = this.dashRepo.findAllWorkPlans();
		List<Double> workloads = baseWorkloads.stream().map(WorkPlan::getWorkloadHours).collect(Collectors.toList());
		List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(workloads);
		return formattedWorkloads.stream().mapToDouble(a->a).average().getAsDouble();
		
	}
	
	@Transactional
	public Double deviationOfWorkPlanWorkload() {
		List<WorkPlan> baseWorkloads = this.dashRepo.findAllWorkPlans();
		List<Double> workloads = baseWorkloads.stream().map(WorkPlan::getWorkloadHours).collect(Collectors.toList());
		List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(workloads);
		return AdministratorDashboardService.sd(formattedWorkloads);
	}
	
	public static List<Double> formatTime(List<Double> baseWorkloads) {
		List<Double> formattedWorkloads = new ArrayList<>();
		for (int i = 0; i < baseWorkloads.size();i++) {
			Double workload = baseWorkloads.get(i);
			Double newWorkload = WorkLoadOperations.unformatWorkload(workload);
			formattedWorkloads.add(newWorkload);
		}
		return formattedWorkloads;
	}

	
	public static Double sd (List<Double> table)	{
	  
	    double mean = table.stream().mapToDouble(a->a).average().getAsDouble();
	    double temp = 0;

	    for (int i = 0; i < table.size(); i++) {
	         double val = table.get(i);
	         double squrDiffToMean = Math.pow(val - mean, 2);
	        temp += squrDiffToMean;
	    }
	    double meanOfDiffs = temp / (table.size());

	    return Math.sqrt(meanOfDiffs);
	}

	@Transactional
	public Double getShoutXXXRateInXXX(){
		List<Shout> shouts=dashRepo.getAllShouts();
		return  Double.valueOf(shouts.stream().filter(x->x.getXxx()!=null&&x.getXxx().getCurrency()!=null).count())/shouts.size();

	}
}
