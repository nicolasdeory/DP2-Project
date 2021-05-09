/*
 * AuthenticatedUserAccountUpdateService.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.useraccount;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.components.Response;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.entities.UserRole;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractUpdateService;

@Service
public class AuthenticatedUserAccountUpdateService implements AbstractUpdateService<Authenticated, UserAccount> {

	// Internal state ---------------------------------------------------------

	private static final String PASSWORD = "password";
	private static final String CONFIRMATION = "confirmation";
	private static final String MASKED_PASSWORD = "[MASKED-PASWORD]";

	@Autowired
	protected AuthenticatedUserAccountRepository repository;

	// AbstractUpdateService<Authenticated, UserAccount> interface ------------


	@Override
	public boolean authorise(final Request<UserAccount> request) {
		AssertUtils.assertRequestNotNull(request);

		return true;
	}

	@Override
	public void bind(final Request<UserAccount> request, final UserAccount entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		String password;

		request.bind(entity, errors, "username", PASSWORD);
		password = request.getModel().getString(PASSWORD);
		if (!password.equals(MASKED_PASSWORD)) {
			entity.setPassword(password);
		}
	}

	@Override
	public void unbind(final Request<UserAccount> request, final UserAccount entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);

		request.unbind(entity, model, "username", "identity.name", "identity.surname", "identity.email");

		if (request.isMethod(HttpMethod.POST)) {
			request.transfer(model, PASSWORD);
			request.transfer(model, CONFIRMATION);
		} else {
			request.unbind(entity, model, PASSWORD);
			model.setAttribute(PASSWORD, MASKED_PASSWORD);
			model.setAttribute(CONFIRMATION, MASKED_PASSWORD);
		}
	}

	@Override
	public UserAccount findOne(final Request<UserAccount> request) {
		AssertUtils.assertRequestNotNull(request);

		UserAccount result;
		Principal principal;

		principal = request.getPrincipal();
		result = this.repository.findOneUserAccountById(principal.getAccountId());

		return result;
	}

	@Override
	public void validate(final Request<UserAccount> request, final UserAccount entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		int passwordLength;
		String password;
		String confirmation;
		boolean isMatching;

		passwordLength = request.getModel().getString(PASSWORD).length();
		errors.state(request, passwordLength >= 5 && passwordLength <= 60, PASSWORD, "acme.validation.length", 6, 60);

		password = request.getModel().getString(PASSWORD);
		confirmation = request.getModel().getString(CONFIRMATION);
		isMatching = password.equals(confirmation);
		errors.state(request, isMatching, CONFIRMATION, "anonymous.user-account.error.confirmation-no-match");
	}

	@Override
	public void update(final Request<UserAccount> request, final UserAccount entity) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);

		this.repository.save(entity);
		for (final UserRole role : entity.getRoles()) {
			this.repository.save(role);
		}
	}

	@Override
	public void onSuccess(final Request<UserAccount> request, final Response<UserAccount> response) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertResponseNotNull(response);

		if (request.isMethod(HttpMethod.POST)) {
			PrincipalHelper.handleUpdate();
		}
	}

}
