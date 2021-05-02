package acme.features.manager.workPlan;

import java.util.Collection;
import java.util.Date;

import acme.entities.tasks.Task;
import acme.framework.entities.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.workPlan.WorkPlan;
import acme.framework.repositories.AbstractRepository;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

@Repository
public interface ManagerWorkPlanRepository extends AbstractRepository {
    //@Query("select workplan from WorkPlan workplan where workplan.isPublic = true and workplan.executionPeriod.finishDateTime>=CURRENT_DATE order by workplan.executionPeriod")
    //Collection<WorkPlan> findAuthenticatedPublicWorkPlan();

    //and workplan.executionPeriod.finishDateTime>=CURRENT_DATE order by workplan.executionPeriod
    @Query("select workplan from WorkPlan workplan where workplan.user.id = ?1 and workplan.executionPeriod.finishDateTime>=CURRENT_DATE order by workplan.executionPeriod")
    Collection<WorkPlan> findAuthenticatedOwnWorkPlan(Integer id);

    @Query("select workplan from WorkPlan workplan where workplan.id = ?1")
    WorkPlan findOneWorkPlanById(Integer id);

    @Query("select task from Task task where task.id = ?1")
    Task findOneTaskById(Integer id);

    @Query("select task.executionPeriod.startDateTime from Task task left join task.workPlans w where w.id = ?1 order by task.executionPeriod.startDateTime")
    Date findFirstStartDate(Integer id);

    @Query("select task.executionPeriod.finishDateTime from Task task left join task.workPlans w where w.id = ?1 order by task.executionPeriod.finishDateTime DESC ")
    Date findLastFinishDate(Integer id);

    @Query("select user from UserAccount user where user.id =?1")
    UserAccount findUserById(Integer id);

    @Query("select DISTINCT task from Task task where task.user.id =?1")
    Collection<Task> findTasksByUserIdAndNotInWorkplan(Integer userId);


}


