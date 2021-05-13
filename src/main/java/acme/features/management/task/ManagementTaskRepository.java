package acme.features.management.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tasks.Task;
import acme.entities.workplan.WorkPlan;
import acme.framework.entities.UserAccount;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface ManagementTaskRepository extends AbstractRepository {

	@Query("select task from Task task where task.isPublic = true AND task.executionPeriod.finishDateTime<CURRENT_TIMESTAMP ORDER BY task.executionPeriod")
	Collection<Task> findPublicAndFinishedTasks();
	
	@Query("select task from Task task where task.id = ?1")
	Task findOneTaskById(Integer id);

	@Query("select task from Task task where task.user.id = ?1 order by task.executionPeriod")
	Collection<Task> findAuthenticatedOwnTasks(Integer id);
	
	@Query("select workplan from WorkPlan workplan")
	Collection<WorkPlan> findWorkPlans();
	
	@Query("select user from UserAccount user where user.id =?1")
	UserAccount findUserById(Integer id);
	
}