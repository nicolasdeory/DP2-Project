/*
 * SpamParameterService.java
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

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.components.Response;
import acme.framework.entities.Administrator;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractUpdateService;

@Service
public class SpamParametersUpdateService implements AbstractUpdateService<Administrator, SpamParameters> {

	private static final String KEYWORDS = "keywords";

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SpamParametersRepository repository;



	@Override
	public boolean authorise(final Request<SpamParameters> request) {
		AssertUtils.assertRequestNotNull(request);

		return true;
	}

	@Override
	public void bind(final Request<SpamParameters> request, final SpamParameters entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<SpamParameters> request, final SpamParameters entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);

		request.unbind(entity, model, "threshold", KEYWORDS);
	}

	@Override
	public SpamParameters findOne(final Request<SpamParameters> request) {
		AssertUtils.assertRequestNotNull(request);

		SpamParameters result;
		result = this.repository.findAllSpamParameters().stream().collect(Collectors.toList()).get(0);

		return result;
	}

	@Override
	public void validate(final Request<SpamParameters> request, final SpamParameters entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		Optional<String> tooShort = entity.getKeywords().stream().filter(x -> x.length() < 2).findFirst();
		Optional<String> tooLong = entity.getKeywords().stream().filter(x -> x.length() > 100).findFirst();
		if (tooShort.isPresent())
			errors.state(request, false, KEYWORDS, "administrator.spam.form.error.wordTooShort", tooShort.get());
		if (tooLong.isPresent())
			errors.state(request, false, KEYWORDS, "administrator.spam.form.error.wordTooLong");

		if(errors.hasErrors()){
			this.unbind(request,entity,request.getModel());
		}
	}

	@Override
	public void update(final Request<SpamParameters> request, final SpamParameters entity) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);

		final Set<String> set = entity.getKeywords().stream().collect(Collectors.toSet());
		entity.setKeywords(set.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new)));

		this.repository.save(entity);
	}

	@Override
	public void onSuccess(final Request<SpamParameters> request, final Response<SpamParameters> response) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertResponseNotNull(response);

		if (request.isMethod(HttpMethod.POST)) {
			PrincipalHelper.handleUpdate();
		}
	}

}
