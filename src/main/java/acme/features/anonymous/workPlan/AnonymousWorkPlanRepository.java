
package acme.features.anonymous.workPlan;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.workPlan.WorkPlan;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnonymousWorkPlanRepository extends AbstractRepository {

	@Query("select workplan from WorkPlan workplan where workplan.isPublic = true order by workplan.executionPeriod")
	Collection<WorkPlan> findAnonymousPublicWorkPlan();

}
