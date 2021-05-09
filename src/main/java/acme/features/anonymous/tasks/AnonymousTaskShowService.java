package acme.features.anonymous.tasks;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.entities.Anonymous;
import acme.entities.tasks.Task;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractShowService;

@Service
public class AnonymousTaskShowService implements AbstractShowService<Anonymous, Task> {

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

		// revisar
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
		request.unbind(entity, model, "title", "isPublic", "description", "link");
		model.setAttribute("workload", entity.getWorkload());
		model.setAttribute("isFinished", entity.isFinished());

	}

	@Override
	public Task findOne(final Request<Task> request) {
		AssertUtils.assertRequestNotNull(request);

		Task task;
		int id;

		id = request.getModel().getInteger("id");
		task = this.repository.findOneById(id);

		return task;
	}

}
