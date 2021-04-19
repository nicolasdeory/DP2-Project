/*
 * AuthenticatedProviderUpdateService.java
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

import acme.entities.roles.Provider;
import acme.features.authenticated.provider.AuthenticatedProviderRepository;
import acme.framework.components.*;
import acme.framework.entities.Administrator;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SpamParametersUpdateService implements AbstractUpdateService<Administrator, SpamParameters> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SpamParametersRepository repository;

	// AbstractUpdateService<Authenticated, Provider> interface ---------------


	@Override
	public boolean authorise(final Request<SpamParameters> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<SpamParameters> request, final SpamParameters entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<SpamParameters> request, final SpamParameters entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "threshold", "keywords");
	}

	@Override
	public SpamParameters findOne(final Request<SpamParameters> request) {
		assert request != null;

		SpamParameters result;
		result = this.repository.findAllSpamParameters().stream().collect(Collectors.toList()).get(0);

		return result;
	}

	@Override
	public void validate(final Request<SpamParameters> request, final SpamParameters entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}

	@Override
	public void update(final Request<SpamParameters> request, final SpamParameters entity) {
		assert request != null;
		assert entity != null;

		String kw = (String)request.getModel().get(0).get("newKeyword");
		if (kw != null && kw.length() > 0)
		{
			entity.keywords.add(kw);
		}

		this.repository.save(entity);
	}

	@Override
	public void onSuccess(final Request<SpamParameters> request, final Response<SpamParameters> response) {
		assert request != null;
		assert response != null;

		if (request.isMethod(HttpMethod.POST)) {
			PrincipalHelper.handleUpdate();
		}
	}

}
