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

import acme.entities.mocke.Mocke;
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
        request.bind(entity.getMocke(), errors);

    }

    @Override
    public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "author", "text", "info");
        request.unbind(entity.getMocke(), model, "bow", "budget",
                "important", "deadline");
    }

    @Override
    public Shout instantiate(final Request<Shout> request) {
        AssertUtils.assertRequestNotNull(request);

        Shout result;
        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);

        result = new Shout();
        result.setMoment(moment);
        result.setMocke(new Mocke());

        return result;
    }

    @Override
    public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        final Mocke mocke = entity.getMocke();

        if(mocke.getDeadline()==null){
            errors.state(request,false,"deadline","anonymous.shout.mocke.error.deadline.null");
        }

        if (mocke.getBudget() != null
                && (!(mocke.getBudget().getCurrency().equals("EUR")
                        || mocke.getBudget().getCurrency().equals("USD")|| mocke.getBudget().getCurrency().equals("GBP")))) {
            errors.state(request, false, "budget",
                    "anonymous.shout.mocke.error.budget.format");
        }

        if (mocke.getBudget() != null
                && mocke.getBudget().getAmount() < 0) {
            errors.state(request, false, "budget",
                    "anonymous.shout.mocke.error.budget.negative");
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.WEEK_OF_YEAR, +1);
        if (mocke.getDeadline() != null
                && mocke.getDeadline().before(c.getTime())) {
            errors.state(request, false, "deadline",
                    "anonymous.shout.mocke.error.deadline.week");
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
        final Date date = new Date(System.currentTimeMillis() - 1);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_MONTH, +1);

        entity.getMocke().setShout(entity);

        // Estrategia para generar un identificador aleatorio. Escogemos un número aleatorio basado en la hora del sistema
        // y la fecha. Lo adaptamos al patrón que nos piden.
        String randInt = String.valueOf(System.currentTimeMillis());

        entity.getMocke().setBow(randInt.substring(randInt.length() - 6) + ":" + year.substring(2) + ":"
                + (month.length() == 1 ? "0" : "") + month + ":" + (day.length() == 1 ? "0" : "") + day);
        this.repository.save(entity);

    }

}
