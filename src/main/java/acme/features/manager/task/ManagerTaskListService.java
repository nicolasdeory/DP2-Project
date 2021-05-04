package acme.features.manager.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Manager;
import acme.entities.tasks.Task;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractListService;

@Service
public class ManagerTaskListService implements AbstractListService<Manager, Task> {

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

		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
		request.unbind(entity, model, "title", "description", "isPublic", "link"); 
		model.setAttribute("workload", entity.getWorkload());
	}

	@Override
	public Collection<Task> findMany(final Request<Task> request) {
		Collection<Task> result;
		final Integer userId = Integer.valueOf(request.getPrincipal().getAccountId());
		result = this.repository.findAuthenticatedOwnTasks(userId);
		return result;
	}

}
