package acme.features.manager.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.roles.Manager;
import acme.entities.tasks.Task;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;

@Controller
@RequestMapping("/manager/task/")
public class ManagerTaskController extends AbstractController<Manager, Task> {

    // Internal state ---------------------------------------------------------

    @Autowired
    protected ManagerTaskListService taskListService;
    
    @Autowired
    protected ManagerTaskShowService showTaskService;

    @Autowired
    protected ManagerTaskUpdateService updateTaskService;
    
    @Autowired
    protected ManagerTaskCreateService createTaskService;
    
    @Autowired
    protected ManagerTaskDeleteService deleteTaskService;
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
