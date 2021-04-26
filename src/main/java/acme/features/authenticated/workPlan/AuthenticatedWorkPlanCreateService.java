package acme.features.authenticated.workPlan;

import acme.datatypes.ExecutionPeriod;
import acme.framework.entities.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractCreateService;

import java.util.Date;

@Service
public class AuthenticatedWorkPlanCreateService implements AbstractCreateService<Authenticated, WorkPlan>{

	@Autowired
	protected AuthenticatedWorkPlanRepository repository;
	
	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		ExecutionPeriod executionPeriod=new ExecutionPeriod();
		request.bind(entity, errors);
		request.bind(executionPeriod,errors);
		entity.setExecutionPeriod(executionPeriod);

		
		
	}

	@Override
	public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
		request.unbind(entity, model, "title","description","tasks","isPublic");
		
	}

	@Override
	public WorkPlan instantiate(final Request<WorkPlan> request) {
		assert request != null;

		WorkPlan workPlan;
		workPlan = new WorkPlan();
		workPlan.setExecutionPeriod(new ExecutionPeriod());

		
		return workPlan;
	}

	@Override
	public void validate(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;		
		
		//TODO
	}

	@Override
	public void create(final Request<WorkPlan> request, final WorkPlan entity) {
		assert request != null;
		assert entity != null;
		UserAccount user= repository.findUserById(request.getPrincipal().getAccountId());
		entity.setUser(user);
		this.repository.save(entity);
	}


}
