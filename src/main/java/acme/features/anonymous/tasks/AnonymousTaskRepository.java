package acme.features.administrator.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.entities.Task;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnonymousTaskRepository extends AbstractRepository {

	@Query("select task from Task task where task.isPublic = true")
	Collection<Task> findPublicTasks();

}
