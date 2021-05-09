package acme.features.authenticated.management;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Management;
import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.components.Response;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractCreateService;

@Service
public class AuthenticatedManagementCreateService implements AbstractCreateService<Authenticated, Management> {


    @Autowired
    protected AuthenticatedManagementRepository repository;

    @Override
    public boolean authorise(final Request<Management> request) {
        AssertUtils.assertRequestNotNull(request);

        return true;
    }

    @Override
    public void bind(final Request<Management> request, final Management entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);

        request.bind(entity, errors);
    }

    @Override
    public void unbind(final Request<Management> request, final Management entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "team");
    }

    @Override
    public Management instantiate(final Request<Management> request) {
        Management result;
        Principal principal;
        int userAccountId;
        UserAccount userAccount;

        principal = request.getPrincipal();
        userAccountId = principal.getAccountId();
        userAccount = this.repository.findOneUserAccountById(userAccountId);

        result = new Management();
        result.setUserAccount(userAccount);

        return result;
    }

    @Override
    public void validate(final Request<Management> request, final Management entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
    }

    @Override
    public void create(final Request<Management> request, final Management entity) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        this.repository.save(entity);
    }
    @Override
    public void onSuccess(final Request<Management> request, final Response<Management> response) {
        AssertUtils.assertRequestNotNull(request);
        assert response != null;

        if (request.isMethod(HttpMethod.POST)) {
            PrincipalHelper.handleUpdate();
        }
    }
}
