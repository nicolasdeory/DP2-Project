/*
 * AnonymousShoutCreateService.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.anonymous.shout;

import java.time.LocalDate;
import java.util.Date;

import acme.entities.XXX.XXX;
import acme.utils.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.shouts.Shout;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractCreateService;

@Service
public class AnonymousShoutCreateService implements AbstractCreateService<Anonymous, Shout> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnonymousShoutRepository repository;

	// AbstractCreateService<Administrator, Shout> interface --------------

	@Override
	public boolean authorise(final Request<Shout> request) {

		AssertUtils.assertRequestNotNull(request);

		return true;
	}

	@Override
	public void bind(final Request<Shout> request, final Shout entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);

		request.bind(entity, errors);
		request.bind(entity.getXxx(),errors);
	}

	@Override
	public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertModelNotNull(model);

		request.unbind(entity, model, "author", "text", "info");
		request.unbind(entity.getXxx(),model,"Xdate","currency","XXXflag");
	}

	@Override
	public Shout instantiate(final Request<Shout> request) {
		AssertUtils.assertRequestNotNull(request);

		Shout result;
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);

		result = new Shout();
		result.setAuthor("John Doe");
		result.setText("Lorem ipsum!");
		result.setMoment(moment);
		result.setInfo("http://example.org");
		result.setXxx(new XXX());



		return result;
	}

	@Override
	public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);
		AssertUtils.assertErrorsNotNull(errors);
		String currency=entity.getXxx().getCurrency().getCurrency();
		if(!(currency.equals("XX")||currency.equals("YY"))){
			errors.state(request,false,"currency","anonymous.shout.XXX.error.currency.format");
		}

		java.sql.Date date=new java.sql.Date(entity.getXxx().getXdate().getTime());
		Boolean isDuplicated = this.repository.findXXX(date).orElse(null) != null;
		errors.state(request,!isDuplicated,"Xdate","anonymous.shout.XXX.error.Xdate.duplicated");
	}

	@Override
	public void create(final Request<Shout> request, final Shout entity) {
		AssertUtils.assertRequestNotNull(request);
		AssertUtils.assertEntityNotNull(entity);

		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setMoment(moment);
		entity.getXxx().setShoutMoment(moment);
		entity.getXxx().setShout(entity);
		this.repository.save(entity);
		this.repository.flush();
	}

}
