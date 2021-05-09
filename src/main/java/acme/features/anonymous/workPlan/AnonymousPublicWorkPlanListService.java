
package acme.features.anonymous.workPlan;

import java.util.Collection;
import java.util.stream.Collectors;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractListService;

@Service
public class AnonymousPublicWorkPlanListService implements AbstractListService<Anonymous, WorkPlan> {

	@Autowired
	protected AnonymousWorkPlanRepository repository;


	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		AssertUtils.assertRequestNotNull(request);
		return true;
	}

	@Override
	public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);
		request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
		request.unbind(entity, model, "title");
		model.setAttribute("workload", entity.getWorkloadHours());

	}

	@Override
	public Collection<WorkPlan> findMany(final Request<WorkPlan> request) {
		AssertUtils.assertRequestNotNull(request);
		Collection<WorkPlan> result;
		result = this.repository.findAnonymousPublicWorkPlan();
		return result;
	}

}
