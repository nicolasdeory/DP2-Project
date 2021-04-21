package acme.features.authenticated.workPlan;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractListService;

@Service
public class AuthenticatedWorkPlanListService implements AbstractListService<Authenticated,WorkPlan>{

	@Autowired
	protected AuthenticatedWorkPlanRepository repository;
	
	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		assert request != null;
		
		return true;
	}

	@Override
	public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
		request.unbind(entity, model, "title"); 
		model.setAttribute("workload", entity.getWorkloadHours()); 

		
	}

	@Override
	public Collection<WorkPlan> findMany(final Request<WorkPlan> request) {
		Collection<WorkPlan> result;
		final Integer userId = Integer.valueOf(request.getPrincipal().getAccountId());
		result = this.repository.findAuthenticatedOwnWorkPlan(userId);
		return result;
	}

}
