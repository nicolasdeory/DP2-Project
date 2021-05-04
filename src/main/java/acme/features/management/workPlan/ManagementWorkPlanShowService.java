package acme.features.management.workPlan;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.entities.workPlan.WorkPlan;
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
            ispublic=true;
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
}
