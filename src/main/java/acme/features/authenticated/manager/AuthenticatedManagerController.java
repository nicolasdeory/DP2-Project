package acme.features.authenticated.manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.roles.Manager;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Authenticated;

@Controller
@RequestMapping("/authenticated/manager/")
public class AuthenticatedManagerController extends AbstractController<Authenticated, Manager> {
    // Internal state ---------------------------------------------------------

    @Autowired
    protected AuthenticatedManagerCreateService createService;

    @Autowired
    protected AuthenticatedManagerUpdateService updateService;

    // Constructors -----------------------------------------------------------


    @PostConstruct
    protected void initialise() {
        super.addBasicCommand(BasicCommand.CREATE, this.createService);
        super.addBasicCommand(BasicCommand.UPDATE, this.updateService);
    }

}
