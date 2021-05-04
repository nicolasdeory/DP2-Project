package acme.features.manager.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Manager;
import acme.entities.tasks.Task;
import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractDeleteService;

@Service
public class ManagerTaskDeleteService implements AbstractDeleteService<Manager, Task> {

	@Autowired
	protected ManagerTaskRepository repository;

	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;

		int taskId;
		final Task task;
		UserAccount userAccount;
		Principal principal;

		taskId = request.getModel().getInteger("id");
		task = this.repository.findOneTaskById(taskId);
		userAccount = task.getUser();
		principal = request.getPrincipal();
		if (userAccount.getId() == principal.getAccountId()) {
			return true;
		} else {
			return false;
		}
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

		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "endDateTime");
		request.unbind(entity, model, "title", "isPublic", "executionPeriod", "description", "link");
		model.setAttribute("workload", entity.getWorkload());
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

		for (final WorkPlan workPlan : entity.getWorkPlans()) {
			workPlan.getTasks().remove(entity);
			this.repository.save(workPlan);
		}
		entity.getWorkPlans().clear();

		this.repository.delete(entity);
	}

}
