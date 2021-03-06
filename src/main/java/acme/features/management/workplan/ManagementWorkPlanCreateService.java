package acme.features.management.workplan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractCreateService;

@Service
public class ManagementWorkPlanCreateService implements AbstractCreateService<Management, WorkPlan> {

    private static final String START_DATE_TIME = "startDateTime";
    private static final String FINISH_DATE_TIME = "finishDateTime";

    @Autowired
    protected ManagementWorkPlanRepository repository;

    @Override
    public boolean authorise(final Request<WorkPlan> request) {
        AssertUtils.assertRequestNotNull(request);

        return true;
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
        final List<Task> userTask = this.repository.findTasksByUserId(request.getPrincipal().getAccountId()).stream().collect(Collectors.toList());
        model.setAttribute("userTask", userTask);

    }

    @Override
    public WorkPlan instantiate(final Request<WorkPlan> request) {
        AssertUtils.assertRequestNotNull(request);

        WorkPlan workPlan;
        workPlan = new WorkPlan();
        workPlan.setExecutionPeriod(new ExecutionPeriod());


        return workPlan;
    }

    @Override
    public void validate(final Request<WorkPlan> request, final WorkPlan entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);

        WorkPlanValidator.validateWorkPlan(entity, request, errors, repository);

        if(errors.hasErrors()) {
            this.unbind(request,entity,request.getModel());
        }

    }

    @Override
    public void create(final Request<WorkPlan> request, final WorkPlan entity) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
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
