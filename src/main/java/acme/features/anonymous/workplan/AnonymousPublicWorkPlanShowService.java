package acme.features.anonymous.workplan;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractShowService;

@Service
public class AnonymousPublicWorkPlanShowService implements AbstractShowService<Anonymous, WorkPlan> {

	@Autowired
	protected AnonymousWorkPlanRepository repository;

	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		AssertUtils.assertRequestNotNull(request);
		Integer id=request.getModel().getInteger("id");
		WorkPlan workPlan=repository.findOneById(id);
		return workPlan.getIsPublic() && !workPlan.isFinished();
	}

	@Override
	public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);

		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
		request.unbind(entity, model, "title", "description","tasks");
		model.setAttribute("workload", entity.getWorkloadHours());

		
	}

	@Override
	public WorkPlan findOne(final Request<WorkPlan> request) {
		AssertUtils.assertRequestNotNull(request);
		WorkPlan result;
		int id;

		id = request.getModel().getInteger("id");
		result=repository.findOneById(id);
		return result;
	}


}
