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

import acme.entities.corchu.Corchu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        request.bind(entity.getCorchu(), errors);

    }

    @Override
    public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "author", "text", "info");
        request.unbind(entity.getCorchu(), model, "corchudentifier", "budget",
                "important", "evaluationdate");
    }

    @Override
    public Shout instantiate(final Request<Shout> request) {
        AssertUtils.assertRequestNotNull(request);

        Shout result;
        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);

        result = new Shout();
        result.setMoment(moment);
        result.setCorchu(new Corchu());

        return result;
    }

    @Override
    public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        final Corchu corchu = entity.getCorchu();

        if (corchu.getEvaluationdate() == null)
        {
            errors.state(request, false, "evaluationdate", "anonymous.shout.corchu.error.evaluationdate.null");
        }

        if (corchu.getBudget() != null
                && (!(corchu.getBudget().getCurrency().equals("EUR")
                        || corchu.getBudget().getCurrency().equals("USD")|| corchu.getBudget().getCurrency().equals("GBP")))) {
            errors.state(request, false, "budget",
                    "anonymous.shout.corchu.error.budget.format");
        }

        if (corchu.getBudget() != null
                && corchu.getBudget().getAmount() < 0) {
            errors.state(request, false, "budget",
                    "anonymous.shout.corchu.error.budget.negative");
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.WEEK_OF_YEAR, +1);
        if (corchu.getEvaluationdate() != null
                && corchu.getEvaluationdate().before(c.getTime())) {
            errors.state(request, false, "evaluationdate",
                    "anonymous.shout.corchu.error.evaluationdate.week");
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

        entity.getCorchu().setShout(entity);

        // Estrategia para generar un identificador aleatorio. Escogemos un número aleatorio basado en la hora del sistema
        // y la fecha. Lo adaptamos al patrón que nos piden.
        String randInt = String.valueOf(System.currentTimeMillis());

//        entity.getCorchu().setCorchudentifier(randInt.substring(randInt.length() - 6) + ":" + year + ":"
//                + (month.length() == 1 ? "0" : "") + month + ":" + (day.length() == 1 ? "0" : "") + day);
        entity.getCorchu().setCorchudentifier(year.substring(2) + "." + (month.length() == 1 ? "0" : "") + month + "." + (day.length() == 1 ? "0" : "") + day + "$" + randInt.substring(randInt.length() - 3));
        this.repository.save(entity);

    }

}
