package acme.features.authenticated.management;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.roles.Management;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Authenticated;

@Controller
@RequestMapping("/authenticated/management/")
public class AuthenticatedManagementController extends AbstractController<Authenticated, Management> {
    // Internal state ---------------------------------------------------------

    @Autowired
    protected AuthenticatedManagementCreateService createService;

    @Autowired
    protected AuthenticatedManagementUpdateService updateService;

    // Constructors -----------------------------------------------------------


    @PostConstruct
    protected void initialise() {
        super.addBasicCommand(BasicCommand.CREATE, this.createService);
        super.addBasicCommand(BasicCommand.UPDATE, this.updateService);
    }

}
