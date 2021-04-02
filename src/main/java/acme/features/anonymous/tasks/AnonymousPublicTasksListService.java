package acme.features.anonymous.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Task;
import acme.framework.entities.UserRole;
import acme.framework.services.AbstractCreateService;

@Service
public class AnonymousPublicTasksListService implements AbstractListService<Anonymous, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnonymousTaskRepository repository;


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
		// TODO
	}

    @Override
	public Collection<Task> findPublicNonFinishedTasks(final Request<Task> request) {
		assert request != null;
		Collection<Task> result;
		result = this.repository.findPublicTasks();

		return result.stream().filter(task -> task.getIsFinished());
	}

}
