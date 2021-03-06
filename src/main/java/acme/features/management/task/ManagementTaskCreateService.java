package acme.features.management.task;

import java.util.Date;

import acme.utils.AssertUtils;
import acme.utils.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.ExecutionPeriod;
import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractCreateService;


@Service
public class ManagementTaskCreateService implements AbstractCreateService<Management, Task> {

    private static final String START_DATE_TIME = "startDateTime";
    private static final String FINISH_DATE_TIME = "finishDateTime";
    private static final String WORKLOAD = "workload";

    @Autowired
    protected ManagementTaskRepository repository;

    @Override
    public boolean authorise(final Request<Task> request) {
        AssertUtils.assertRequestNotNull(request);

        return true;

    }

    @Override
    public void bind(final Request<Task> request, final Task entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        final ExecutionPeriod executionPeriod = new ExecutionPeriod();

        request.bind(entity, errors);

        if (request.getModel().hasAttribute(START_DATE_TIME)) {
            try {
                executionPeriod.setStartDateTime(request.getModel().getAttribute(START_DATE_TIME, Date.class));
            } catch (final Exception e) {
                errors.state(request, false, START_DATE_TIME, "management.tasks.error.startDateTime.format");
            }
        }
        if (request.getModel().hasAttribute(FINISH_DATE_TIME)) {
            try {
                executionPeriod.setFinishDateTime(request.getModel().getAttribute(FINISH_DATE_TIME, Date.class));
            } catch (final Exception e) {
                errors.state(request, false, FINISH_DATE_TIME, "management.tasks.error.finishDate.format");
            }

        }
        entity.setExecutionPeriod(executionPeriod);

    }

    @Override
    public void unbind(final Request<Task> request, final Task entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        //revisar

        request.unbind(entity.getExecutionPeriod(), model, START_DATE_TIME, FINISH_DATE_TIME);
        request.unbind(entity, model, "title", "isPublic", "description", "link", WORKLOAD);

    }

    @Override
    public Task instantiate(final Request<Task> request) {
        AssertUtils.assertRequestNotNull(request);

        Task task;
        task = new Task();
        task.setExecutionPeriod(new ExecutionPeriod());

        return task;
    }

    @Override
    public void validate(final Request<Task> request, final Task entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);

        TaskValidator.validateTask(entity, request, errors);

    }

    @Override
    public void create(final Request<Task> request, final Task entity) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);

        final UserAccount user = this.repository.findUserById(request.getPrincipal().getAccountId());
        entity.setUser(user);


        this.repository.save(entity);

    }
}
