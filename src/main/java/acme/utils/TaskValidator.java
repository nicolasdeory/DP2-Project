package acme.utils;

import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Request;

import java.util.Date;

public final class TaskValidator {

    private static final String START_DATE_TIME = "startDateTime";
    private static final String FINISH_DATE_TIME = "finishDateTime";
    private static final String WORKLOAD = "workload";

    private TaskValidator() {
    }

    public static void validateTask(Task entity, Request<Task> request, Errors errors) {
        boolean validDates = validateStartDateNotNull(entity, request, errors) && validateFinishDateNotNull(entity, request, errors);

        if (validDates) {
            validateStartDateAfterNow(entity, request, errors);
            validateFinishDateAfterNow(entity, request, errors);
            validateFinishDateAfterStartDate(entity, request, errors);
        }
        if (entity.getWorkPlans() != null)
            validateIsPublic(entity, request, errors);
        boolean validWorkload = validateWorkload(entity, request, errors);
        boolean workloadNotNull = entity.getWorkload() != null && !errors.hasErrors(WORKLOAD);
        boolean datesHaveNoErrors = !errors.hasErrors(START_DATE_TIME) && !errors.hasErrors(FINISH_DATE_TIME);
        if (validWorkload && workloadNotNull && datesHaveNoErrors) {
            validateMaxWorkload(entity, request, errors);
        }

    }

    private static void validateIsPublic(Task entity, Request<Task> request, Errors errors) {
        boolean validIsPublic = entity.getWorkPlans().stream().anyMatch(x -> x.getIsPublic() && !entity.getIsPublic());
        if (validIsPublic) {
            errors.state(request, false, "isPublic", "management.tasks.error.isPublic");
        }
    }

    private static boolean validateStartDateNotNull(Task entity, Request<Task> request, Errors errors) {
        if (entity.getExecutionPeriod().getStartDateTime() == null) {
            errors.state(request, false, START_DATE_TIME, "management.tasks.error.startDate.empty");
            return false;
        }
        return true;
    }

    private static boolean validateFinishDateNotNull(Task entity, Request<Task> request, Errors errors) {
        if (entity.getExecutionPeriod().getFinishDateTime() == null) {
            errors.state(request, false, FINISH_DATE_TIME, "management.tasks.error.finishDate.empty");
            return false;
        }
        return true;
    }

    private static void validateStartDateAfterNow(Task entity, Request<Task> request, Errors errors) {
        final Date now = new Date(System.currentTimeMillis());

        if (entity.getExecutionPeriod().getStartDateTime().before(now)) {
            errors.state(request, false, START_DATE_TIME, "management.tasks.error.startDate");
        }
    }

    private static void validateFinishDateAfterNow(Task entity, Request<Task> request, Errors errors) {
        final Date now = new Date(System.currentTimeMillis());

        if (entity.getExecutionPeriod().getFinishDateTime().before(now)) {
            errors.state(request, false, FINISH_DATE_TIME, "management.tasks.error.finishDate");
        }
    }

    private static void validateFinishDateAfterStartDate(Task entity, Request<Task> request, Errors errors) {
        if (entity.getExecutionPeriod().getStartDateTime().after(entity.getExecutionPeriod().getFinishDateTime())) {
            errors.state(request, false, START_DATE_TIME, "management.tasks.error.startDate.after");
            errors.state(request, false, FINISH_DATE_TIME, "management.tasks.error.finishDate.before");
        }
    }

    private static boolean validateWorkload(Task entity, Request<Task> request, Errors errors) {
        if (entity.getWorkload() == null || !WorkLoadOperations.isFormatedWorkload(entity.getWorkload())) {
            errors.state(request, false, WORKLOAD, "management.tasks.workload.error.format");
            return false;
        }
        return true;
    }

    private static void validateMaxWorkload(Task entity, Request<Task> request, Errors errors) {
        Double maxWorkload = WorkLoadOperations.formatWorkload(entity.getExecutionPeriod().getWorkloadHours());
        if (entity.getWorkload() > maxWorkload) {
            errors.state(request, false, WORKLOAD, "management.tasks.workload.error.workloadMax");
        }
    }
}
