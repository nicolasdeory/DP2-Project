package acme.features.anonymous.workPlan;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.workPlan.WorkPlan;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractListService;

public class AnonymousPublicWorkPlanListService implements AbstractListService<Anonymous,WorkPlan> {

	@Autowired
	protected AnonymousWorkPlansRepository repository;
	
	@Override
	public boolean authorise(final Request<WorkPlan> request) {
		assert request != null;
		return true;
	}

	@Override
	public void unbind(final Request<WorkPlan> request, final WorkPlan entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model !=null;
		request.unbind(entity, model, "title","description","isPublic");
		
	}

	@Override
	public Collection<WorkPlan> findMany(final Request<WorkPlan> request) {
		assert request != null;
		Collection<WorkPlan> result;
		result = this.repository.findAnonymousPublicWorkPlan();
		return result;
	}

}