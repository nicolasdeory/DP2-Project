package acme.features.administrator.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tasks.Task;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorTaskRepository extends AbstractRepository {

	@Query("select task from Task task where task.isPublic = true ORDER BY task.getWorkload DESC")
	Collection<Task> findPublicTasks();

}