package acme.features.anonymous.workPlan;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.workPlan.WorkPlan;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnonymousWorkPlansRepository extends AbstractRepository {
	
	@Query("select workplan from WorkPlan workplan where workplan.isPublic = true and workplan.isFinished= false order by workplan.executionPeriod.finishDateTime")
	Collection<WorkPlan> findAnonymousPublicWorkPlan();
	
}
