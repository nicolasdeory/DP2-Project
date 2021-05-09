package acme.features.anonymous.tasks;

import java.util.Collection;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tasks.Task;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractListService;

@Service
public class AnonymousPublicTasksListService implements AbstractListService<Anonymous, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnonymousTaskRepository repository;

	@Override
	public boolean authorise(final Request<Task> request) {
		AssertUtils.assertRequestNotNull(request);

		return true;
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
		model.setAttribute("workload", entity.getWorkload());
		request.unbind(entity, model, "title", "description", "isPublic", "link");
	}

	@Override
	public Collection<Task> findMany(final Request<Task> request) {
		AssertUtils.assertRequestNotNull(request);
		return this.repository.findNoFinishedAndPublicTasks();
	}
}
