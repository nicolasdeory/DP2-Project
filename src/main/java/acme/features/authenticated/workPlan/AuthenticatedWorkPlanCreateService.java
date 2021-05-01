package acme.features.authenticated.workPlan;

import acme.datatypes.ExecutionPeriod;
import acme.entities.tasks.Task;
import acme.framework.entities.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractCreateService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticatedWorkPlanCreateService implements AbstractCreateService<Authenticated, WorkPlan> {

    @Autowired
    protected AuthenticatedWorkPlanRepository repository;

    @Override
    public boolean authorise(final Request<WorkPlan> request) {
        assert request != null;

        return true;
    }

    @Override
    public void bind(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;
        ExecutionPeriod executionPeriod = new ExecutionPeriod();
        request.bind(entity, errors);
        request.bind(executionPeriod, errors);
        entity.setExecutionPeriod(executionPeriod);


    }

    @Override
    public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
        assert request != null;
        assert entity != null;
        assert model != null;

        request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
        request.unbind(entity, model, "title", "description", "tasks", "isPublic");
        List<Task> userTask = repository.findTasksByUserIdAndNotInWorkplan(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
        model.setAttribute("userTask", userTask);

    }

    @Override
    public WorkPlan instantiate(final Request<WorkPlan> request) {
        assert request != null;

        WorkPlan workPlan;
        workPlan = new WorkPlan();
        workPlan.setExecutionPeriod(new ExecutionPeriod());


        return workPlan;
    }

    @Override
    public void validate(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;
        List<String> newTask = new ArrayList<>(entity.getNewTasksId());
        for (String taskId : newTask) {
            Integer id = Integer.valueOf(taskId);
            Task t = repository.findOneTaskById(id);
            if (entity.getExecutionPeriod().getStartDateTime().after(t.getExecutionPeriod().getStartDateTime())) {
                errors.add("startDateTime", "authenticated.workplan.error.startDate");
            }
            if (entity.getExecutionPeriod().getFinishDateTime().before(t.getExecutionPeriod().getFinishDateTime())) {
                errors.add("finishDateTime", "authenticated.workplan.error.finishDate");
            }
        }


    }

    @Override
    public void create(final Request<WorkPlan> request, final WorkPlan entity) {
        assert request != null;
        assert entity != null;
        UserAccount user = repository.findUserById(request.getPrincipal().getAccountId());
        entity.setUser(user);
        entity.setTasks(new ArrayList<>());
        for (String taskId : entity.getNewTasksId()) {
            Integer id = Integer.valueOf(taskId);
            Task t = repository.findOneTaskById(id);
            t.getWorkPlans().add(entity);
            entity.getTasks().add(t);

        }
        this.repository.save(entity);
        this.repository.saveAll(entity.getTasks());
    }


}
