package acme.features.authenticated.workPlan;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Authenticated;


@Controller
@RequestMapping("/authenticated/workplan/")
public class AuthenticatedWorkPlanController extends AbstractController<Authenticated, WorkPlan>{

	
	// Internal state ---------------------------------------------------------
	
	@Autowired
	protected AuthenticatedWorkPlanCreateService createService;
	
	@Autowired
	protected AuthenticatedWorkPlanDeleteService deleteService;
		
	@Autowired
	protected AuthenticatedWorkPlanListService listService;
	
	@Autowired
	protected AuthenticatedWorkPlanShowService showService;
		
	@Autowired
	protected AuthenticatedWorkPlanUpdateService updateService;
	
	
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
