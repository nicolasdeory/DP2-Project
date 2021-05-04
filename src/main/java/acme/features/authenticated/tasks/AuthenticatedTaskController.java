package acme.features.authenticated.tasks;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Authenticated;
import acme.entities.tasks.Task;

@Controller
@RequestMapping("/authenticated/task/")
public class AuthenticatedTaskController extends AbstractController<Authenticated, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected AuthenticatedListPublicFinishedTasksService listPublicTasksService;

    @Autowired
    protected AuthenticatedTaskShowService showTaskService;

    // Constructors -----------------------------------------------------------

    @PostConstruct
    protected void initialise() {
        super.addBasicCommand(BasicCommand.LIST, this.listPublicTasksService);
        super.addBasicCommand(BasicCommand.SHOW, this.showTaskService);
    }

}
