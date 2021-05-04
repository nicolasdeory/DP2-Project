package acme.features.manager.workPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.ExecutionPeriod;
import acme.entities.roles.Manager;
import acme.entities.tasks.Task;
import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractUpdateService;

@Service
public class ManagerWorkPlanUpdateService implements AbstractUpdateService<Manager, WorkPlan> {

    @Autowired
    protected ManagerWorkPlanRepository repository;

    @Override
    public boolean authorise(final Request<WorkPlan> request) {
        assert request != null;
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

        final ExecutionPeriod executionPeriod = new ExecutionPeriod();
        request.bind(entity, errors);
        if(request.getModel().hasAttribute("startDateTime")){
            try{
                executionPeriod.setStartDateTime(request.getModel().getAttribute("startDateTime",Date.class));
            }
            catch(final Exception e){
                errors.state(request,false,"startDateTime","manager.workplan.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute("finishDateTime")){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute("finishDateTime",Date.class));
            }catch(final Exception e){
                errors.state(request,false,"finishDateTime","manager.workplan.error.finishDate.format");
            }

        }
        entity.setExecutionPeriod(executionPeriod);

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
        Boolean ispublic=entity.getIsPublic();
        if(entity.getIsPublic()==null){
            ispublic=false;
        }
        final List<Task> userTask;
        if(ispublic==true){
            userTask = this.repository.findTasksByUserIdIsPublic(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
        }else{
            userTask = this.repository.findTasksByUserId(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
        }

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
        final Date now=new Date(System.currentTimeMillis());
        if(entity.getExecutionPeriod().getStartDateTime()!=null&&entity.getExecutionPeriod().getFinishDateTime()!=null){
            if(entity.getExecutionPeriod().getStartDateTime().before(now) ){
                errors.state(request,false,"startDateTime", "manager.workplan.error.startDate");

            }
            if(entity.getExecutionPeriod().getFinishDateTime().before(now)){
                errors.state(request,false,"finishDateTime", "manager.workplan.error.finishDate");
            }
            if(entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())){
                errors.state(request,false,"startDateTime","manager.workplan.error.startDate.after");
                errors.state(request,false,"finishDateTime","manager.workplan.error.finishDate.before");
            }
        }else{
            if(entity.getExecutionPeriod().getStartDateTime()==null){
                errors.state(request,false,"startDateTime", "manager.workplan.error.startDate.empty");
            }
            if(entity.getExecutionPeriod().getFinishDateTime()==null){
                errors.state(request,false,"finishDateTime", "manager.workplan.error.finishDate.empty");
            }

        }

        List<String> newTask = new ArrayList<>();
        newTask=entity.getNewTasksId();
        if(newTask!=null&&!errors.hasErrors()){
            Boolean startDateError=true;
            Boolean finishDateError=true;
            Boolean isPublicError=true;
            for (final String taskId : newTask) {
                final Integer id = Integer.valueOf(taskId);
                final Task t = this.repository.findOneTaskById(id);
                if (startDateError&&entity.getExecutionPeriod().getStartDateTime().after(t.getExecutionPeriod().getStartDateTime())) {
                    errors.state(request,false,"startDateTime", "manager.workplan.error.startDate.task");
                    startDateError=false;
                }
                if (finishDateError&&entity.getExecutionPeriod().getFinishDateTime().before(t.getExecutionPeriod().getFinishDateTime())) {
                    errors.state(request,false,"finishDateTime", "manager.workplan.error.finishDate.task");
                    finishDateError=false;
                }
                if(isPublicError&&!((entity.getIsPublic()&& t.getIsPublic()) || !entity.getIsPublic())){
                    errors.state(request,false,"isPublic", "manager.workplan.error.isPublic");
                    isPublicError=false;
                }
            }

        }
        if(errors.hasErrors()){
            this.unbind(request,entity,request.getModel());
        }
    }

    @Override
    public void update(final Request<WorkPlan> request, final WorkPlan entity) {
        assert request != null;
        assert entity != null;
        final List<Task> tasks = entity.getTasks();
        if (entity.getNewTasksId() == null || entity.getNewTasksId().size() == 0) {
            tasks.clear();
        } else {
            final Set<Integer> newTaskIdStrings = entity.getNewTasksId().stream()
                    .map(x -> Integer.valueOf(x))
                    .collect(Collectors.toSet());

            final Map<Integer, Task> idTask = new HashMap<>();
            for (final Task t : tasks) {
                idTask.put(t.getId(), t);
            }

            for (final Integer taskId : idTask.keySet()) {
                if (!newTaskIdStrings.contains(taskId)) {
                    final Task task = idTask.get(taskId);
                    tasks.remove(task);

                } else {
                    // ya hemos terminado de manejarla, la borramos para evitar iteraciones innecesarias
                    newTaskIdStrings.remove(taskId);
                }
            }

            for (final Integer taskId : newTaskIdStrings) {
                if (!idTask.keySet().contains(taskId)) {
                    // podría optimizarse si JPA permitiera añadir un ManyToMany por id, sin tener que traerse el objeto
                    final Task taskToAdd = this.repository.findOneTaskById(taskId);
                    tasks.add(taskToAdd);
                }
            }
        }
        this.repository.save(entity);
    }

}
