package acme.features.management.workPlan;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractListService;

@Service
public class ManagementWorkPlanListService implements AbstractListService<Management, WorkPlan> {

    @Autowired
    protected ManagementWorkPlanRepository repository;

    @Override
    public boolean authorise(final Request<WorkPlan> request) {
        assert request != null;
        return true;

    }

    @Override
    public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
        assert request != null;
        assert entity != null;
        assert model != null;
        request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
        request.unbind(entity, model, "title");
        model.setAttribute("workload", entity.getWorkloadHours());
    }

    @Override
    public Collection<WorkPlan> findMany(final Request<WorkPlan> request) {
        Collection<WorkPlan> result;
        final Integer userId = Integer.valueOf(request.getPrincipal().getAccountId());
        result = this.repository.findAuthenticatedOwnWorkPlan(userId);
        return result;
    }

}
