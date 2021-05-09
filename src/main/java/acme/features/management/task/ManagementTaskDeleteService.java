package acme.features.management.task;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.entities.workplan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractDeleteService;

@Service
public class ManagementTaskDeleteService implements AbstractDeleteService<Management, Task> {

	@Autowired
	protected ManagementTaskRepository repository;

	@Override
	public boolean authorise(final Request<Task> request) {
		AssertUtils.assertRequestNotNull(request);

		int taskId;
		final Task task;
		UserAccount userAccount;
		Principal principal;

		taskId = request.getModel().getInteger("id");
		task = this.repository.findOneTaskById(taskId);
		userAccount = task.getUser();
		principal = request.getPrincipal();
		return userAccount.getId() == principal.getAccountId();
	}

	@Override
	public void bind(final Request<Task> request, final Task entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		request.bind(entity, errors);

	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);

		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "endDateTime");
		request.unbind(entity, model, "title", "isPublic", "executionPeriod", "description", "link");
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

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

	}

	@Override
	public void delete(final Request<Task> request, final Task entity) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);

		for (final WorkPlan workPlan : entity.getWorkPlans()) {
			workPlan.getTasks().remove(entity);
			this.repository.save(workPlan);
		}
		entity.getWorkPlans().clear();

		this.repository.delete(entity);
	}

}
