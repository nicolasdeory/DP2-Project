package acme.features.authenticated.tasks;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.entities.tasks.Task;
import acme.features.manager.task.ManagerTaskRepository;
import acme.framework.services.AbstractListService;

@Service
public class AuthenticatedListPublicFinishedTasksService implements AbstractListService<Authenticated, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected ManagerTaskRepository repository;

    @Override
    public boolean authorise(final Request<Task> request) {
        assert request != null;

        return true;
    }

    @Override
    public void unbind(final Request<Task> request, final Task entity, final Model model) {
        assert request != null;
        assert entity != null;
        assert model != null;
        request.unbind(entity.getExecutionPeriod(), model, "startDateTime", "finishDateTime");
        model.setAttribute("workload", entity.getWorkload());
        request.unbind(entity, model, "title", "description", "isPublic");
    }

    @Override
    public Collection<Task> findMany(Request<Task> request) {
        assert request != null;
        return this.repository.findPublicAndFinishedTasks();
    }
}
