package acme.features.management.workPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.ExecutionPeriod;
import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractCreateService;

@Service
public class ManagementWorkPlanCreateService implements AbstractCreateService<Management, WorkPlan> {

    @Autowired
    protected ManagementWorkPlanRepository repository;

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
                errors.state(request,false,"startDateTime","management.workplan.error.startDateTime.format");
            }
        }
        if(request.getModel().hasAttribute("finishDateTime")){
            try{
                executionPeriod.setFinishDateTime(request.getModel().getAttribute("finishDateTime",Date.class));
            }catch(final Exception e){
                errors.state(request,false,"finishDateTime","management.workplan.error.finishDate.format");
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
        final List<Task> userTask = this.repository.findTasksByUserId(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
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
            if(entity.getExecutionPeriod().getStartDateTime().before(now) ){
                errors.state(request,false,"startDateTime", "management.workplan.error.startDate");
            }
            if(entity.getExecutionPeriod().getFinishDateTime().before(now)){
                errors.state(request,false,"finishDateTime", "management.workplan.error.finishDate");
            }
            if(entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())){
                errors.state(request,false,"startDateTime","management.workplan.error.startDate.after");
                errors.state(request,false,"finishDateTime","management.workplan.error.finishDate.before");
            }
        }else{
            if(entity.getExecutionPeriod().getStartDateTime()==null){
                errors.state(request,false,"startDateTime", "management.workplan.error.startDate.empty");
            }
            if(entity.getExecutionPeriod().getFinishDateTime()==null){
                errors.state(request,false,"finishDateTime", "management.workplan.error.finishDate.empty");
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
                    errors.state(request,false,"startDateTime", "management.workplan.error.startDate.task");
                    startDateError=false;
                }
                if (finishDateError&&entity.getExecutionPeriod().getFinishDateTime().before(t.getExecutionPeriod().getFinishDateTime())) {
                    errors.state(request,false,"finishDateTime", "management.workplan.error.finishDate.task");
                    finishDateError=false;
                }
                if(isPublicError&&!((entity.getIsPublic()&& t.getIsPublic()) || !entity.getIsPublic())){
                    errors.state(request,false,"isPublic", "management.workplan.error.isPublic");
                    isPublicError=false;
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
        //ToDo Las tareas privadas no pueden estar en workplan publicos
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
