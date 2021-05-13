package acme.utils;

import acme.entities.tasks.Task;
import acme.entities.workplan.WorkPlan;
import acme.features.management.workplan.ManagementWorkPlanRepository;
import acme.framework.components.Errors;
import acme.framework.components.Request;

import java.util.Date;

public class WorkPlanValidator {

    private static final String START_DATE_TIME = "startDateTime";
    private static final String FINISH_DATE_TIME = "finishDateTime";

    private WorkPlanValidator() { }

    public static void validateWorkPlan(WorkPlan entity, Request<WorkPlan> request, Errors errors, ManagementWorkPlanRepository repository)
    {
        boolean validDates = validateStartDateNotNull(entity, request, errors) && validateFinishDateNotNull(entity, request, errors);

        if (validDates)
        {
            validateStartDateAfterNow(entity, request, errors);
            validateFinishDateAfterNow(entity, request, errors);
            validateFinishDateAfterStartDate(entity, request, errors);
        }

        if (entity.getNewTasksId() != null && !errors.hasErrors())
            validateTasks(entity, request, errors, repository);

    }

    private static boolean validateStartDateNotNull(WorkPlan entity, Request<WorkPlan> request, Errors errors)
    {
        if (entity.getExecutionPeriod().getStartDateTime()==null){
            errors.state(request,false,START_DATE_TIME, "management.workplan.error.startDate.empty");
            return false;
        }
        return true;
    }

    private static boolean validateFinishDateNotNull(WorkPlan entity, Request<WorkPlan> request, Errors errors)
    {
        if (entity.getExecutionPeriod().getFinishDateTime()==null){
            errors.state(request,false,FINISH_DATE_TIME, "management.workplan.error.finishDate.empty");
            return false;
        }
        return true;
    }

    private static void validateStartDateAfterNow(WorkPlan entity, Request<WorkPlan> request, Errors errors)
    {
        final Date now = new Date(System.currentTimeMillis());

        if (entity.getExecutionPeriod().getStartDateTime().before(now) ){
            errors.state(request,false,START_DATE_TIME, "management.workplan.error.startDate");
        }
    }

    private static void validateFinishDateAfterNow(WorkPlan entity, Request<WorkPlan> request, Errors errors)
    {
        final Date now = new Date(System.currentTimeMillis());

        if (entity.getExecutionPeriod().getFinishDateTime().before(now)){
            errors.state(request,false,FINISH_DATE_TIME, "management.workplan.error.finishDate");
        }
    }

    private static void validateFinishDateAfterStartDate(WorkPlan entity, Request<WorkPlan> request, Errors errors)
    {
        if (entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())){
            errors.state(request,false,START_DATE_TIME,"management.workplan.error.startDate.after");
            errors.state(request,false,FINISH_DATE_TIME,"management.workplan.error.finishDate.before");
        }
    }

    private static void validateTasks(WorkPlan entity, Request<WorkPlan> request, Errors errors, ManagementWorkPlanRepository repository)
    {
        boolean alreadyHandledStartDateError=false;
        boolean alreadyHandledFinishDateError=false;
        boolean alreadyHandledPublicError=false;
        for (final String taskId : entity.getNewTasksId()) {
            final Integer id = Integer.valueOf(taskId);
            final Task t = repository.findOneTaskById(id);
            if (!alreadyHandledStartDateError && entity.getExecutionPeriod().getStartDateTime().after(t.getExecutionPeriod().getStartDateTime())) {
                errors.state(request,false,START_DATE_TIME, "management.workplan.error.startDate.task");
                alreadyHandledStartDateError=true;
            }
            if (!alreadyHandledFinishDateError && entity.getExecutionPeriod().getFinishDateTime().before(t.getExecutionPeriod().getFinishDateTime())) {
                errors.state(request,false,FINISH_DATE_TIME, "management.workplan.error.finishDate.task");
                alreadyHandledFinishDateError=true;
            }
            if(!alreadyHandledPublicError && !((entity.getIsPublic()&& t.getIsPublic()) || !entity.getIsPublic())){
                errors.state(request,false,"isPublic", "management.workplan.error.isPublic");
                alreadyHandledPublicError=true;
            }
        }
    }
}
