package acme.features.management.workPlan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractDeleteService;

@Service
public class ManagementWorkPlanDeleteService implements AbstractDeleteService<Management, WorkPlan> {

    @Autowired
    protected ManagementWorkPlanRepository repository;


    @Override
    public boolean authorise(final Request<WorkPlan> request) {
        assert request != null;
        final boolean result;
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
        request.unbind(entity, model, "title", "description", "tasks");
        model.setAttribute("workload", entity.getWorkloadHours());
        model.setAttribute("isFinished", entity.isFinished());
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

        //TODO
    }

    @Override
    public void delete(final Request<WorkPlan> request, final WorkPlan entity) {
        assert request != null;
        assert entity != null;
        for (final Task task : entity.getTasks()) {
            task.getWorkPlans().remove(entity);
            this.repository.save(task);
        }
        entity.getTasks().clear();

        this.repository.delete(entity);
    }

}