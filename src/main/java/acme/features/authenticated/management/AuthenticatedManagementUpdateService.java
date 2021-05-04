package acme.features.authenticated.management;


import acme.entities.roles.Management;
import acme.framework.components.*;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedManagementUpdateService implements AbstractUpdateService<Authenticated, Management> {

    @Autowired
    protected AuthenticatedManagementRepository repository;

    @Override
    public boolean authorise(Request<Management> request) {
        assert request != null;

        return true;
    }

    @Override
    public void bind(Request<Management> request, Management entity, Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;

        request.bind(entity, errors);
    }

    @Override
    public void unbind(Request<Management> request, Management entity, Model model) {
        assert request != null;
        assert entity != null;
        assert model != null;

        request.unbind(entity, model, "team");
    }

    @Override
    public Management findOne(Request<Management> request) {
        assert request != null;

        Management result;
        Principal principal;
        int userAccountId;

        principal = request.getPrincipal();
        userAccountId = principal.getAccountId();

        result = this.repository.findOneManagementByUserAccountId(userAccountId);

        return result;
    }

    @Override
    public void validate(Request<Management> request, Management entity, Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;
    }

    @Override
    public void update(Request<Management> request, Management entity) {
        assert request != null;
        assert entity != null;

        this.repository.save(entity);
    }

    @Override
    public void onSuccess(final Request<Management> request, final Response<Management> response) {
        assert request != null;
        assert response != null;

        if (request.isMethod(HttpMethod.POST)) {
            PrincipalHelper.handleUpdate();
        }
    }
}
