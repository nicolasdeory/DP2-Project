
package acme.features.anonymous.workPlan;

import java.util.Collection;

import acme.datatypes.ExecutionPeriod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.entities.workPlan.WorkPlan;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnonymousWorkPlanRepository extends AbstractRepository {

	@Query("select workplan from WorkPlan workplan where workplan.isPublic = true and workplan.executionPeriod.finishDateTime>=CURRENT_DATE order by workplan.executionPeriod")
	Collection<WorkPlan> findAnonymousPublicWorkPlan();

	@Query("select w from WorkPlan w where w.id = ?1")
	WorkPlan findOneById(Integer id);
}
