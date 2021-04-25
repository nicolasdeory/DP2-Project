package acme.features.administrator.tasks;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Administrator;
import acme.entities.tasks.Task;

@Controller
@RequestMapping("/administrator/tasks/")
public class AdministratorTaskController extends AbstractController<Administrator, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected AdministratorListPublicFinishedTasksService listPublicTasksService;

    // Constructors -----------------------------------------------------------

    @PostConstruct
    protected void initialise() {
        super.addBasicCommand(BasicCommand.LIST, this.listPublicTasksService);
    }

}
