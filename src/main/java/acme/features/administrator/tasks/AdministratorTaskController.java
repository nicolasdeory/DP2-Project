package acme.features.administrator.tasks;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Anonymous;
import acme.entities.tasks.Task;

@Controller
@RequestMapping("/anonymous/tasks/")
public class AdministratorTaskController extends AbstractController<Anonymous, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected AdministratorListPublicFinishedTasksService listPublicTasksService;

    // Constructors -----------------------------------------------------------

    @PostConstruct
    protected void initialise() {
        super.addBasicCommand(BasicCommand.LIST, this.listPublicTasksService);
    }

}
