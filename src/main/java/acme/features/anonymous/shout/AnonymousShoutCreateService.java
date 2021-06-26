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
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.deolet.Deolet;
import acme.entities.shouts.Shout;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Anonymous;
import acme.framework.services.AbstractCreateService;
import acme.utils.AssertUtils;

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
        request.bind(entity.getDeolet(), errors);

    }

    @Override
    public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "author", "text", "info");
        request.unbind(entity.getDeolet(), model, "tracker", "budget", "important");
    }

    @Override
    public Shout instantiate(final Request<Shout> request) {
        AssertUtils.assertRequestNotNull(request);

        Shout result;
        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);

        result = new Shout();
        result.setMoment(moment);
        result.setDeolet(new Deolet());

        return result;
    }

    @Override
    public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        final Deolet deolet = entity.getDeolet();
        if (deolet.getBudget().getCurrency() != null
                && (!(deolet.getBudget().getCurrency().equals("EUR") || deolet.getBudget().getCurrency().equals("USD")))) {
            errors.state(request, false, "budget", "anonymous.shout.deolet.error.budget.format");
        }

        if (deolet.getBudget().getCurrency() != null && deolet.getBudget().getAmount() <= 0)
        {
            errors.state(request, false, "budget", "anonymous.shout.deolet.error.budget.negative");
        }

    }

    @Override
    public void create(final Request<Shout> request, final Shout entity) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);

        final LocalDate now = LocalDate.now();
        final String year = String.valueOf(now.getYear());
        final String month = String.valueOf(now.getMonthValue());
        final String day = String.valueOf(now.getDayOfMonth());

        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);
        entity.setMoment(moment);
        final Date Xdate = new Date(System.currentTimeMillis() - 1);
        final Calendar c = Calendar.getInstance();
        c.setTime(Xdate);
        c.add(Calendar.MONTH, +1);
        entity.getDeolet().setDeadline(c.getTime());
        entity.getDeolet().setShout(entity);

        entity.getDeolet()
                .setTracker(year + (month.length() == 1 ? "0" : "") + month + (day.length() == 1 ? "0" : "") + day + "0");
        this.repository.save(entity);
        this.repository.flush();
        entity.getDeolet().setTracker(entity.getDeolet().getTracker() + entity.getId());
        this.repository.save(entity);
    }

}
