package acme.features.manager.workPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractCreateService;

@Service
public class ManagerWorkPlanCreateService implements AbstractCreateService<Manager, WorkPlan> {

    @Autowired
    protected ManagerWorkPlanRepository repository;

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
        final ExecutionPeriod executionPeriod = new ExecutionPeriod();
        request.bind(entity, errors);
        if(request.getModel().hasAttribute("startDateTime")){
            try{
                executionPeriod.setStartDateTime(request.getModel().getAttribute("startDateTime",Date.class));
            }
            catch(final Exception e){
                errors.add("startDateTime","manager.workplan.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute("finishDateTime")){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute("finishDateTime",Date.class));
            }catch(final Exception e){
                errors.add("finishDateTime","manager.workplan.error.finishDate.format");
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
        final List<Task> userTask = this.repository.findTasksByUserIdAndNotInWorkplan(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
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
        final Date now=new Date(System.currentTimeMillis());
        if(entity.getExecutionPeriod().getStartDateTime()!=null&&entity.getExecutionPeriod().getFinishDateTime()!=null){
            if(!errors.hasErrors("startDateTime")&&entity.getExecutionPeriod().getStartDateTime().before(now) ){
                errors.add("startDateTime", "manager.workplan.error.startDate");
            }
            if(entity.getExecutionPeriod().getFinishDateTime().before(now)){
                errors.add("finishDateTime", "manager.workplan.error.finishDate");
            }
            if(entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())){
                errors.add("startDateTime","manager.workplan.error.startDate.after");
                errors.add("finishDateTime","manager.workplan.error.finishDate.before");
            }
        }else{
            if(entity.getExecutionPeriod().getStartDateTime()==null){
                errors.add("startDateTime", "manager.workplan.error.startDate.empty");
            }
            if(entity.getExecutionPeriod().getFinishDateTime()==null){
                errors.add("finishDateTime", "manager.workplan.error.finishDate.empty");
            }

        }

        List<String> newTask = new ArrayList<>();
        newTask=entity.getNewTasksId();
        if(newTask!=null&&!errors.hasErrors()){
            for (final String taskId : newTask) {
                final Integer id = Integer.valueOf(taskId);
                final Task t = this.repository.findOneTaskById(id);
                if (entity.getExecutionPeriod().getStartDateTime().after(t.getExecutionPeriod().getStartDateTime())) {
                    errors.add("startDateTime", "manager.workplan.error.startDate.task");
                }
                if (entity.getExecutionPeriod().getFinishDateTime().before(t.getExecutionPeriod().getFinishDateTime())) {
                    errors.add("finishDateTime", "manager.workplan.error.finishDate.task");
                }
            }

        }
        if(errors.hasErrors()){
            this.unbind(request,entity,request.getModel());
        }



    }

    @Override
    public void create(final Request<WorkPlan> request, final WorkPlan entity) {
        assert request != null;
        assert entity != null;
        final UserAccount user = this.repository.findUserById(request.getPrincipal().getAccountId());
        entity.setUser(user);
        entity.setTasks(new ArrayList<>());
        if(entity.getNewTasksId()!=null){
            for (final String taskId : entity.getNewTasksId()) {
                final Integer id = Integer.valueOf(taskId);
                final Task t = this.repository.findOneTaskById(id);
                t.getWorkPlans().add(entity);
                entity.getTasks().add(t);

            }
        }

        this.repository.save(entity);
        this.repository.saveAll(entity.getTasks());
    }


}
