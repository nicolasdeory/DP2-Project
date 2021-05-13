package acme.features.management.task;

import java.util.Collection;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractListService;

@Service
public class ManagementTaskListService implements AbstractListService<Management, Task> {

	@Autowired
	protected ManagementTaskRepository repository;

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
