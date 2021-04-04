package acme.features.administrator.tasks;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.entities.tasks.Task;
import acme.framework.services.AbstractListService;

@Service
public class AdministratorListPublicFinishedTasksService implements AbstractListService<Anonymous, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected AdministratorTaskRepository repository;

    @Override
    public boolean authorise(final Request<Task> request) {
        assert request != null;

        return true;
    }

    @Override
    public void unbind(final Request<Task> request, final Task entity, final Model model) {
        // TODO
    }

    @Override
    public Collection<Task> findMany(Request<Task> request) {
        assert request != null;
        Collection<Task> result;
        result = this.repository.findPublicTasks();

        return result.stream().filter(task -> task.isFinished()).collect(Collectors.toList());
    }
}
