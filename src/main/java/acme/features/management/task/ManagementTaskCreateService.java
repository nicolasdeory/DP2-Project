package acme.features.management.task;

import java.util.Date;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.utils.WorkLoadOperations;
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

        if (request.getModel().hasAttribute("startDateTime")) {
            try {
                executionPeriod.setStartDateTime(request.getModel().getAttribute("startDateTime", Date.class));
            } catch (final Exception e) {
                errors.state(request, false, "startDateTime", "management.tasks.error.startDateTime.format");
            }
        }
        if (request.getModel().hasAttribute("finishDateTime")) {
            try {
                executionPeriod.setFinishDateTime(request.getModel().getAttribute("finishDateTime", Date.class));
            } catch (final Exception e) {
                errors.state(request, false, "finishDateTime", "management.tasks.error.finishDate.format");
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

        request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
        request.unbind(entity, model, "title", "isPublic", "description", "link", "workload");

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

        final Date now = new Date(System.currentTimeMillis());

        if (entity.getExecutionPeriod().getStartDateTime() != null && entity.getExecutionPeriod().getFinishDateTime() != null) {
            if (entity.getExecutionPeriod().getStartDateTime().before(now)) {
                errors.state(request, false, "startDateTime", "management.tasks.error.startDate");
            }
            if (entity.getExecutionPeriod().getFinishDateTime().before(now)) {
                errors.state(request, false, "finishDateTime", "management.tasks.error.finishDate");
            }
            if (entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())) {
                errors.state(request, false, "startDateTime", "management.tasks.error.startDate.after");
                errors.state(request, false, "finishDateTime", "management.tasks.error.finishDate.before");
            }
        } else {
            if (entity.getExecutionPeriod().getStartDateTime() == null) {
                errors.state(request, false, "startDateTime", "management.tasks.error.startDate.empty");
            }
            if (entity.getExecutionPeriod().getFinishDateTime() == null) {
                errors.state(request, false, "finishDateTime", "management.tasks.error.finishDate.empty");
            }

        }

        if (entity.getWorkload() != null&&!errors.hasErrors("workload")) {
            Boolean isValid = WorkLoadOperations.isFormatedWorkload(entity.getWorkload());
            if (isValid && !errors.hasErrors("startDateTime") && !errors.hasErrors("finishDateTime")) {
                Double maxWorkload = WorkLoadOperations.formatWorkload(entity.getExecutionPeriod().getWorkloadHours());
                if (entity.getWorkload() > maxWorkload) {
                    errors.state(request, false, "workload", "management.tasks.workload.error.workloadMax");
                }
            }
            if (!isValid) {
                errors.state(request, false, "workload", "management.tasks.workload.error.format");
            }
        }


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
