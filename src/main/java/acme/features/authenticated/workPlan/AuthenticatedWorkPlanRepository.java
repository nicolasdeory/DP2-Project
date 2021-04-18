package acme.features.authenticated.workPlan;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.workPlan.WorkPlan;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedWorkPlanRepository extends AbstractRepository {
	@Query("select workplan from WorkPlan workplan where workplan.user.id = ?1")
	Collection<WorkPlan> findAuthenticatedPublicWorkPlan(Integer id);
	
	@Query("select workplan from WorkPlan workplan where workplan.id = ?1")
	WorkPlan findOneWorkPlanById(Integer id);
}


