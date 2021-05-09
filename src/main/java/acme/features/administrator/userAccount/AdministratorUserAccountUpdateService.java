/*
 * AdministratorUserAccountUpdateService.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.userAccount;

import java.util.Collection;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.entities.UserAccount;
import acme.framework.entities.UserAccountStatus;
import acme.framework.entities.UserRole;
import acme.framework.services.AbstractUpdateService;

@Service
public class AdministratorUserAccountUpdateService implements AbstractUpdateService<Administrator, UserAccount> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorUserAccountRepository repository;

	// AbstractUpdateService<Administrator, UserAccount> interface -------------


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

		StringBuilder buffer;
		Collection<UserRole> roles;

		request.unbind(entity, model, "username", "identity.name", "identity.surname", "identity.email");

		roles = entity.getRoles();
		buffer = new StringBuilder();
		for (final UserRole role : roles) {
			buffer.append(role.getAuthorityName());
			buffer.append(" ");
		}

		model.setAttribute("roleList", buffer.toString());

		if (entity.isEnabled()) {
			model.setAttribute("status", UserAccountStatus.ENABLED);
		} else {
			model.setAttribute("status", UserAccountStatus.DISABLED);
		}
	}

	@Override
	public UserAccount findOne(final Request<UserAccount> request) {
		AssertUtils.assertRequestNotNull(request);

		UserAccount result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneUserAccountById(id);

		return result;
	}

	@Override
	public void validate(final Request<UserAccount> request, final UserAccount entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);
	}

	@Override
	public void update(final Request<UserAccount> request, final UserAccount entity) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);

		if (request.getModel().getString("newStatus").equals("ENABLED")) {
			entity.setEnabled(true);
		} else if (request.getModel().getString("newStatus").equals("DISABLED")) {
			entity.setEnabled(false);
		}

		this.repository.save(entity);
	}

}
