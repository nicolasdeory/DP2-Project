package acme.features.authenticated.manager;


import acme.entities.roles.Manager;
import acme.framework.components.*;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedManagerUpdateService implements AbstractUpdateService<Authenticated, Manager> {

    @Autowired
    protected AuthenticatedManagerRepository repository;

    @Override
    public boolean authorise(Request<Manager> request) {
        assert request != null;

        return true;
    }

    @Override
    public void bind(Request<Manager> request, Manager entity, Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;

        request.bind(entity, errors);
    }

    @Override
    public void unbind(Request<Manager> request, Manager entity, Model model) {
        assert request != null;
        assert entity != null;
        assert model != null;

        request.unbind(entity, model, "team");
    }

    @Override
    public Manager findOne(Request<Manager> request) {
        assert request != null;

        Manager result;
        Principal principal;
        int userAccountId;

        principal = request.getPrincipal();
        userAccountId = principal.getAccountId();

        result = this.repository.findOneManagerByUserAccountId(userAccountId);

        return result;
    }

    @Override
    public void validate(Request<Manager> request, Manager entity, Errors errors) {
        assert request != null;
        assert entity != null;
        assert errors != null;
    }

    @Override
    public void update(Request<Manager> request, Manager entity) {
        assert request != null;
        assert entity != null;

        this.repository.save(entity);
    }

    @Override
    public void onSuccess(final Request<Manager> request, final Response<Manager> response) {
        assert request != null;
        assert response != null;

        if (request.isMethod(HttpMethod.POST)) {
            PrincipalHelper.handleUpdate();
        }
    }
}
