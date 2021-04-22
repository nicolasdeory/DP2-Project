package acme.features.authenticated.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tasks.Task;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedTaskRepository extends AbstractRepository {

	@Query("select task from Task task where task.isPublic = true AND task.executionPeriod.finishDateTime<CURRENT_TIMESTAMP ORDER BY task.executionPeriod")
	Collection<Task> findPublicAndFinishedTasks();
	
	@Query("select task from Task task where task.id = ?1")
	Task findOneTaskById(Integer id);

	@Query("select task from Task task where task.user.id = ?1 and task.executionPeriod.finishDateTime>=CURRENT_DATE order by task.executionPeriod")
	Collection<Task> findAuthenticatedOwnTasks(Integer id);
}