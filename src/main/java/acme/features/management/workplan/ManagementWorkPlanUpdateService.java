package acme.features.management.workplan;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import acme.utils.AssertUtils;
import acme.utils.WorkPlanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.ExecutionPeriod;
import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.entities.workplan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractUpdateService;

@Service
public class ManagementWorkPlanUpdateService implements AbstractUpdateService<Management, WorkPlan> {

    private static final String START_DATE_TIME = "startDateTime";
    private static final String FINISH_DATE_TIME = "finishDateTime";

    @Autowired
    protected ManagementWorkPlanRepository repository;

    @Override
    public boolean authorise(final Request<WorkPlan> request) {
        AssertUtils.assertRequestNotNull(request);
        int workplanId;
        WorkPlan workPlan;
        UserAccount userAccount;
        Principal principal;

        workplanId = request.getModel().getInteger("id");
        workPlan = this.repository.findOneWorkPlanById(workplanId);
        userAccount = workPlan.getUser();
        principal = request.getPrincipal();
        return userAccount.getId() == principal.getAccountId();

    }

    @Override
    public void bind(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);

        final ExecutionPeriod executionPeriod = new ExecutionPeriod();
        request.bind(entity, errors);
        if(request.getModel().hasAttribute(START_DATE_TIME)){
            try{
                executionPeriod.setStartDateTime(request.getModel().getAttribute(START_DATE_TIME,Date.class));
            }
            catch(final Exception e){
                errors.state(request,false,START_DATE_TIME,"management.workplan.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute(FINISH_DATE_TIME)){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute(FINISH_DATE_TIME,Date.class));
            }catch(final Exception e){
                errors.state(request,false,FINISH_DATE_TIME,"management.workplan.error.finishDate.format");
            }

        }
        entity.setExecutionPeriod(executionPeriod);

    }

    @Override
    public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity.getExecutionPeriod(), model, START_DATE_TIME, FINISH_DATE_TIME);
        request.unbind(entity, model, "title", "description", "tasks", "isPublic");
        model.setAttribute("workload", entity.getWorkloadHours());
        model.setAttribute("isFinished", entity.isFinished());
        boolean isPublic=entity.getIsPublic();
        if(entity.getIsPublic()==null){
            isPublic=false;
        }
        final List<Task> userTask;
        if(isPublic){
            userTask = this.repository.findTasksByUserIdIsPublic(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
        }else{
            userTask = this.repository.findTasksByUserId(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
        }

        userTask.removeAll(entity.getTasks());
        model.setAttribute("userTask", userTask);
    }

    @Override
    public WorkPlan findOne(final Request<WorkPlan> request) {
        AssertUtils.assertRequestNotNull(request);

        WorkPlan workPlan;
        int id;

        id = request.getModel().getInteger("id");
        workPlan = this.repository.findOneWorkPlanById(id);

        return workPlan;
    }

    @Override
    public void validate(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);

        WorkPlanValidator.validateWorkPlan(entity, request, errors, repository);

        if(errors.hasErrors()){
            this.unbind(request,entity,request.getModel());
        }
    }

    @Override
    public void update(final Request<WorkPlan> request, final WorkPlan entity) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        final List<Task> tasks = entity.getTasks();
        if (entity.getNewTasksId() == null || entity.getNewTasksId().isEmpty()) {
            tasks.clear();
        } else {
            final Set<Integer> newTaskIds = entity.getNewTasksId().stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());

            final Map<Integer, Task> mapIdTask = tasks.stream()
                    .collect(Collectors.toMap(Task::getId, Function.identity()));

            removeNotFoundTasks(tasks, newTaskIds, mapIdTask);
            addMissingTasks(tasks, newTaskIds, mapIdTask);

        }
        this.repository.save(entity);
    }

    private void removeNotFoundTasks(List<Task> entityTasks, Set<Integer> newTaskIds, Map<Integer, Task> mapIdTask)
    {
        for (final Map.Entry<Integer, Task> taskEntry : mapIdTask.entrySet()) {
            if (!newTaskIds.contains(taskEntry.getKey())) {
                entityTasks.remove(taskEntry.getValue());

            } else {
                // ya hemos terminado de manejarla, la borramos para evitar iteraciones innecesarias
                newTaskIds.remove(taskEntry.getKey());
            }
        }
    }

    private void addMissingTasks(List<Task> entityTasks, Set<Integer> newTaskIds, Map<Integer, Task> mapIdTask)
    {
        for (final Integer taskId : newTaskIds) {
            if (!mapIdTask.keySet().contains(taskId)) {
                // podría optimizarse si JPA permitiera añadir un ManyToMany por id, sin tener que traerse el objeto
                final Task taskToAdd = this.repository.findOneTaskById(taskId);
                entityTasks.add(taskToAdd);
            }
        }
    }


}
