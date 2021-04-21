package acme.features.authenticated.workPlan;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.workPlan.WorkPlan;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedWorkPlanRepository extends AbstractRepository {
	//@Query("select workplan from WorkPlan workplan where workplan.isPublic = true and workplan.executionPeriod.finishDateTime>=CURRENT_DATE order by workplan.executionPeriod")
	//Collection<WorkPlan> findAuthenticatedPublicWorkPlan();
		
	//and workplan.executionPeriod.finishDateTime>=CURRENT_DATE order by workplan.executionPeriod
	@Query("select workplan from WorkPlan workplan where workplan.user.id = ?1 and workplan.executionPeriod.finishDateTime>=CURRENT_DATE order by workplan.executionPeriod")
	Collection<WorkPlan> findAuthenticatedOwnWorkPlan(Integer id);
	
	@Query("select workplan from WorkPlan workplan where workplan.id = ?1")
	WorkPlan findOneWorkPlanById(Integer id);
}


