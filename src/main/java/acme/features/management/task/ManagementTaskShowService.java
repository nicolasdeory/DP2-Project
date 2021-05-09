package acme.features.management.task;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractShowService;

@Service
public class ManagementTaskShowService implements AbstractShowService<Management, Task> {

	@Autowired
	protected ManagementTaskRepository repository;

	@Override
	public boolean authorise(final Request<Task> request) {
		AssertUtils.assertRequestNotNull(request);

		int taskId;
        Task task;
        UserAccount userAccount;
        Principal principal;

        taskId = request.getModel().getInteger("id");
        task = this.repository.findOneTaskById(taskId);
        userAccount = task.getUser();
        principal = request.getPrincipal();
        return userAccount.getId() == principal.getAccountId();
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
		task = this.repository.findOneTaskById(id);

		return task;
	}

}
