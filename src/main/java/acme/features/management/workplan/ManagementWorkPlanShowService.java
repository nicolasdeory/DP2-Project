package acme.features.management.workplan;

import java.util.List;
import java.util.stream.Collectors;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.entities.workplan.WorkPlan;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractShowService;

@Service
public class ManagementWorkPlanShowService implements AbstractShowService<Management, WorkPlan> {

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
    public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
        request.unbind(entity, model, "title", "description", "tasks", "isPublic");
        model.setAttribute("workload", entity.getWorkloadHours());
        model.setAttribute("isFinished", entity.isFinished());
        boolean isPublic = entity.getIsPublic();
        if(entity.getIsPublic()==null){
            isPublic=true;
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
}
