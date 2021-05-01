package acme.features.authenticated.workPlan;

import acme.entities.tasks.Task;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractUpdateService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticatedWorkPlanUpdateService implements AbstractUpdateService<Authenticated, WorkPlan> {

    @Autowired
    protected AuthenticatedWorkPlanRepository repository;

    @Override
    public boolean authorise(final Request<WorkPlan> request) {
        assert request != null;
        boolean result;
        int workplanId;
        WorkPlan workPlan;
        UserAccount userAccount;
        Principal principal;

        workplanId = request.getModel().getInteger("id");
        workPlan = this.repository.findOneWorkPlanById(workplanId);
        userAccount = workPlan.getUser();
        principal = request.getPrincipal();
        if (userAccount.getId() == principal.getAccountId()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void bind(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;

        request.bind(entity, errors);


    }

    @Override
    public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
        assert request != null;
        assert entity != null;
        assert model != null;

        request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
        request.unbind(entity, model, "title", "description", "tasks", "isPublic");
        model.setAttribute("workload", entity.getWorkloadHours());
        model.setAttribute("isFinished", entity.isFinished());
        List<Task> userTask = repository.findTasksByUserIdAndNotInWorkplan(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
        userTask.removeAll(entity.getTasks());
        model.setAttribute("userTask", userTask);
    }

    @Override
    public WorkPlan findOne(final Request<WorkPlan> request) {
        assert request != null;

        WorkPlan workPlan;
        int id;

        id = request.getModel().getInteger("id");
        workPlan = this.repository.findOneWorkPlanById(id);

        return workPlan;
    }

    @Override
    public void validate(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;
        for (String taskId : entity.getNewTasksId()) {
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
    public void update(final Request<WorkPlan> request, final WorkPlan entity) {
        assert request != null;
        assert entity != null;
        List<Task> tasks = entity.getTasks();
        if (entity.getNewTasksId() == null || entity.getNewTasksId().size() == 0) {
            tasks.clear();
        } else {
            Set<Integer> newTaskIdStrings = entity.getNewTasksId().stream()
                    .map(x -> Integer.valueOf(x))
                    .collect(Collectors.toSet());

            Map<Integer, Task> idTask = new HashMap<>();
            for (Task t : tasks) {
                idTask.put(t.getId(), t);
            }

            for (Integer taskId : idTask.keySet()) {
                if (!newTaskIdStrings.contains(taskId)) {
                    Task task = idTask.get(taskId);
                    tasks.remove(task);

                } else {
                    // ya hemos terminado de manejarla, la borramos para evitar iteraciones innecesarias
                    newTaskIdStrings.remove(taskId);
                }
            }

            for (Integer taskId : newTaskIdStrings) {
                if (!idTask.keySet().contains(taskId)) {
                    // podría optimizarse si JPA permitiera añadir un ManyToMany por id, sin tener que traerse el objeto
                    Task taskToAdd = repository.findOneTaskById(taskId);
                    tasks.add(taskToAdd);
                }
            }
        }
        this.repository.save(entity);
    }

}
