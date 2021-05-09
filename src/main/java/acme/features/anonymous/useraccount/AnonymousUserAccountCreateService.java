/*
 * AnonymousUserAccountCreateService.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.anonymous.useraccount;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.entities.Authenticated;
import acme.framework.entities.UserAccount;
import acme.framework.entities.UserRole;
import acme.framework.services.AbstractCreateService;

@Service
public class AnonymousUserAccountCreateService implements AbstractCreateService<Anonymous, UserAccount> {

	// Internal state ---------------------------------------------------------

	private static final String PASSWORD = "password";
	private static final String CONFIRMATION = "confirmation";
	private static final String ACCEPT = "accept";

	@Autowired
	protected AnonymousUserAccountRepository repository;


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

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<UserAccount> request, final UserAccount entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);

		request.unbind(entity, model, "username", "identity.name", "identity.surname", "identity.email");

		if (request.isMethod(HttpMethod.GET)) {
			model.setAttribute(PASSWORD, "");
			model.setAttribute(CONFIRMATION, "");
			model.setAttribute(ACCEPT, "false");
		} else {
			request.transfer(model, PASSWORD, CONFIRMATION, ACCEPT);
		}
	}

	@Override
	public UserAccount instantiate(final Request<UserAccount> request) {
		AssertUtils.assertRequestNotNull(request);

		UserAccount result;
		Authenticated defaultRole;

		result = new UserAccount();
		result.setEnabled(true);
		defaultRole = new Authenticated();
		result.addRole(defaultRole);
		defaultRole.setUserAccount(result);

		return result;
	}

	@Override
	public void validate(final Request<UserAccount> request, final UserAccount entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		boolean isDuplicated;
		boolean isAccepted;
		boolean isMatching;
		String password;
		String confirmation;
		int passwordLength;

		isDuplicated = this.repository.findOneUserAccountByUsername(entity.getUsername()) != null;
		errors.state(request, !isDuplicated, "username", "anonymous.user-account.error.duplicated");

		passwordLength = request.getModel().getString(PASSWORD).length();
		errors.state(request, passwordLength >= 5 && passwordLength <= 60, PASSWORD, "acme.validation.length", 6, 60);

		isAccepted = request.getModel().getBoolean(ACCEPT);
		errors.state(request, isAccepted, ACCEPT, "anonymous.user-account.error.must-accept");

		password = request.getModel().getString(PASSWORD);
		confirmation = request.getModel().getString(CONFIRMATION);
		isMatching = password.equals(confirmation);
		errors.state(request, isMatching, CONFIRMATION, "anonymous.user-account.error.confirmation-no-match");
	}

	@Override
	public void create(final Request<UserAccount> request, final UserAccount entity) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);

		this.repository.save(entity);
		for (final UserRole role : entity.getRoles()) {
			this.repository.save(role);
		}
	}

}
