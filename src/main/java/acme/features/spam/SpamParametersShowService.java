/*
 * AdministratorUserAccountShowService.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.spam;

import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.*;
import acme.framework.services.AbstractShowService;
import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpamParametersShowService implements AbstractShowService<Administrator, SpamParameters> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SpamParametersRepository repository;

	// AbstractShowService<Administrator, UserAccount> interface --------------


	@Override
	public boolean authorise(final Request<SpamParameters> request) {
		AssertUtils.assertRequestNotNull(request);
		return true;
	}

	@Override
	public void unbind(final Request<SpamParameters> request, final SpamParameters entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);

		request.unbind(entity, model, "threshold", "keywords");
	}

	@Override
	public SpamParameters findOne(final Request<SpamParameters> request) {
		AssertUtils.assertRequestNotNull(request);

		SpamParameters result;
		result = this.repository.findAllSpamParameters().stream().findFirst().orElseThrow(() -> new IllegalStateException("No spam parameters were found."));
		return result;
	}

}
