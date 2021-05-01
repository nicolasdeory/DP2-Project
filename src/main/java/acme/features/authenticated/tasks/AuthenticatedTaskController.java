package acme.features.authenticated.tasks;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.tasks.Task;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Authenticated;

@Controller
@RequestMapping("/authenticated/task/")
public class AuthenticatedTaskController extends AbstractController<Authenticated, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected AuthenticatedTaskListService taskListService;
    
    @Autowired
    protected AuthenticatedTaskShowService showTaskService;

    @Autowired
    protected AuthenticatedTaskUpdateService updateTaskService;
    
    @Autowired
    protected AuthenticatedTaskCreateService createTaskService;
    
    @Autowired
    protected AuthenticatedTaskDeleteService deleteTaskService;
    // Constructors -----------------------------------------------------------

    @PostConstruct
    protected void initialise() {
        super.addBasicCommand(BasicCommand.LIST, this.taskListService);
        super.addBasicCommand(BasicCommand.SHOW, this.showTaskService);
        super.addBasicCommand(BasicCommand.UPDATE, this.updateTaskService);
        super.addBasicCommand(BasicCommand.CREATE, this.createTaskService);
        super.addBasicCommand(BasicCommand.DELETE, this.deleteTaskService);
    }

    
    
}
