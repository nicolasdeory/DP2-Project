package acme.features.management.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.roles.Management;
import acme.entities.tasks.Task;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;

@Controller
@RequestMapping("/management/task/")
public class ManagementTaskController extends AbstractController<Management, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected ManagementTaskListService taskListService;
    
    @Autowired
    protected ManagementTaskShowService showTaskService;

    @Autowired
    protected ManagementTaskUpdateService updateTaskService;
    
    @Autowired
    protected ManagementTaskCreateService createTaskService;
    
    @Autowired
    protected ManagementTaskDeleteService deleteTaskService;
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
