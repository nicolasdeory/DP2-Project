package acme.features.administrator.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Double average = formattedWorkloads.stream().mapToDouble(a->a).average().getAsDouble();
		return AdministratorDashboardService.unformatWorkload(average); 
	
		
		
	}
	
	@Transactional
	public Double deviationOfTaskWorkload() {
		List<Double> baseWorkloads = this.dashRepo.findAllTaskWorkload();
		List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(baseWorkloads);
		Double deviation = AdministratorDashboardService.sd(formattedWorkloads);
		return AdministratorDashboardService.unformatWorkload(deviation);
		
	}
	
	@Transactional
	public Double averageOfWorkPlanWorkload() {
		List<Double> baseWorkloads = this.dashRepo.findAllWorkPlanWorkload();
		List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(baseWorkloads);
		Double average = formattedWorkloads.stream().mapToDouble(a->a).average().getAsDouble();
		return AdministratorDashboardService.unformatWorkload(average); 
		
	}
	
	@Transactional
	public Double deviationOfWorkPlanWorkload() {
		List<Double> baseWorkloads = this.dashRepo.findAllWorkPlanWorkload();
		List<Double> formattedWorkloads = AdministratorDashboardService.formatTime(baseWorkloads);
		Double deviation = AdministratorDashboardService.sd(formattedWorkloads);
		return AdministratorDashboardService.unformatWorkload(deviation);
	
	}
	
	public static List<Double> formatTime(List<Double> baseWorkloads) {
		List<Double> formattedWorkloads = new ArrayList<>();
		for (int i = 0; i < baseWorkloads.size();i++) {
			Double workload = baseWorkloads.get(i);
			Double newWorkload = AdministratorDashboardService.formatWorkload(workload);
			formattedWorkloads.add(newWorkload);
		}
		return formattedWorkloads;
	}
	
	public static Double formatWorkload(Double workload) {
        int hours = workload.intValue();
        double decimals = (workload - hours);
        int minutes = (int) Math.round(Math.floor(decimals * 100) / 100 * 60);
        while (minutes > 59) {
            hours++;
            minutes -= 60;
        }
        return hours + minutes / 100.;
    }
	
	public static Double unformatWorkload(Double workload) {
	    int hours = workload.intValue();
	    int minutes = (int) (workload - hours) * 100;
	    double decimals = minutes / 60;
	    return hours + decimals;
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
}
