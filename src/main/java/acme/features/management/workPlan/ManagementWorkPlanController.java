package acme.features.management.workPlan;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.roles.Management;
import acme.entities.workPlan.WorkPlan;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;


@Controller
@RequestMapping("/management/work-plan/")
public class ManagementWorkPlanController extends AbstractController<Management, WorkPlan>{

	
	// Internal state ---------------------------------------------------------
	
	@Autowired
	protected ManagementWorkPlanCreateService createService;
	
	@Autowired
	protected ManagementWorkPlanDeleteService deleteService;
		
	@Autowired
	protected ManagementWorkPlanListService listService;
	
	@Autowired
	protected ManagementWorkPlanShowService showService;
		
	@Autowired
	protected ManagementWorkPlanUpdateService updateService;
	
	
	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand(BasicCommand.CREATE, this.createService);
		super.addBasicCommand(BasicCommand.DELETE, this.deleteService);
		super.addBasicCommand(BasicCommand.LIST, this.listService);
		super.addBasicCommand(BasicCommand.SHOW, this.showService);
		super.addBasicCommand(BasicCommand.UPDATE, this.updateService);


		
	}

}
