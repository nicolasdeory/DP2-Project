package acme.features.manager.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Manager;
import acme.entities.tasks.Task;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractShowService;

@Service
public class ManagerTaskShowService implements AbstractShowService<Manager, Task>{

	@Autowired
	protected ManagerTaskRepository repository;
	
	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		//revisar
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime"); 
		request.unbind(entity, model, "title","isPublic", "description", "link");
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

}