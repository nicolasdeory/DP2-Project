package acme.features.anonymous.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tasks.Task;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnonymousTaskRepository extends AbstractRepository {

	@Query("select task from Task task where task.isPublic = true AND task.executionPeriod.finishDateTime>=CURRENT_DATE ORDER BY task.executionPeriod")
	Collection<Task> findNoFinishedAndPublicTasks();

}
