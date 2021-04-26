package acme.features.authenticated.workPlan;

import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedWorkPlanShowService implements AbstractShowService<Authenticated, WorkPlan>{

	@Autowired
	protected AuthenticatedWorkPlanRepository repository;

	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		assert request != null;
		boolean result;
		int workplanId;
		WorkPlan workPlan;
		UserAccount userAccount;
		Principal principal;

		workplanId = request.getModel().getInteger("id");
		workPlan = this.repository.findOneWorkPlanById(workplanId);
		userAccount = workPlan.getUser();
		principal = request.getPrincipal();
		if(workPlan.getIsPublic()||userAccount.getId()==principal.getAccountId()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime"); 
		request.unbind(entity, model, "title","description","tasks","isPublic");
		model.setAttribute("workload", entity.getWorkloadHours());
		model.setAttribute("isFinished",entity.isFinished());

		
		
	}

	@Override
	public WorkPlan findOne(final Request<WorkPlan> request) {
		assert request != null;

		WorkPlan workPlan;
		int id;
		
		id = request.getModel().getInteger("id");
		workPlan = this.repository.findOneWorkPlanById(id);
		
		return workPlan;
	}
}
