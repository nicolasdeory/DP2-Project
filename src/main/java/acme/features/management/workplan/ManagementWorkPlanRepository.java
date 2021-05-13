package acme.features.management.workplan;

import java.util.Collection;

import acme.entities.tasks.Task;
import acme.framework.entities.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.workplan.WorkPlan;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface ManagementWorkPlanRepository extends AbstractRepository {

    @Query("select workplan from WorkPlan workplan where workplan.user.id = ?1 order by workplan.executionPeriod")
    Collection<WorkPlan> findAuthenticatedOwnWorkPlan(Integer id);

    @Query("select workplan from WorkPlan workplan where workplan.id = ?1")
    WorkPlan findOneWorkPlanById(Integer id);

    @Query("select task from Task task where task.id = ?1")
    Task findOneTaskById(Integer id);

    @Query("select user from UserAccount user where user.id =?1")
    UserAccount findUserById(Integer id);

    @Query("select task from Task task where task.user.id =?1 and task.isPublic=True")
    Collection<Task> findTasksByUserIdIsPublic(Integer userId);

    @Query("select task from Task task where task.user.id =?1")
    Collection<Task> findTasksByUserId(Integer userId);


}


