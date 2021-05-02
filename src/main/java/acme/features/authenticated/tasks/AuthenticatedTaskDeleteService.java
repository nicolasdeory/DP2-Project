package acme.features.authenticated.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractDeleteService;

@Service
public class AuthenticatedTaskDeleteService implements AbstractDeleteService<Authenticated, Task>{

	@Autowired
	protected AuthenticatedTaskRepository repository;
	
	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
		
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity.getExecutionPeriod(), model,"startDateTime", "endDateTime");
		request.unbind(entity, model, "title","isPublic","executionPeriod", "description", "link");
		model.setAttribute("workload", entity.getWorkloadHours());
		model.setAttribute("isFinished", entity.isFinished());
		
	}

	@Override
	public Task findOne(final Request<Task> request) {
		assert request != null;
		
		Task task;
		int id;
		
		id = request.getModel().getInteger("id");
		task = this.repository.findOneTaskById(id);
		return task;
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;	
		
	}

	@Override
	public void delete(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;
		
		
		this.repository.delete(entity);
	}

}
