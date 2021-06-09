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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.pominok.Pominok;
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
        request.bind(entity.getPominok(), errors);

    }

    @Override
    public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "author", "text", "info");
        request.unbind(entity.getPominok(), model, "marker","deadline", "budget", "important");
    }

    @Override
    public Shout instantiate(final Request<Shout> request) {
        AssertUtils.assertRequestNotNull(request);

        Shout result;
        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);

        result = new Shout();
        result.setMoment(moment);
        result.setPominok(new Pominok());

        return result;
    }

    @Override
    public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        final Pominok xxx = entity.getPominok();
        if (xxx.getBudget() != null
                && (!(xxx.getBudget().getCurrency().equals("EUR") || xxx.getBudget().getCurrency().equals("USD")))) {
            errors.state(request, false, "currency", "anonymous.shout.XXX.error.currency.format");
        }

        if (xxx.getBudget() != null && xxx.getBudget().getAmount() < 0)
        {
            errors.state(request, false, "currency", "anonymous.shout.XXX.error.currency.negative");
        }

        //Si es mas tarde de una semana, error
        final long theFuture = System.currentTimeMillis() + (86400 * 7 * 1000);
        final Date nextWeek = new Date(theFuture);
        
        if(xxx.getDeadline().before(nextWeek)) {
        	errors.state(request, false, "deadline", "anonymous.shout.XXX.error.deadline.before");
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
        
        entity.getPominok().setDeadline(entity.getPominok().getDeadline());
        entity.getPominok().setShout(entity);
        
        
        //set del id ^yymmdd/\d{2}/\w{2}$
        //formateo el año
        final DateFormat df = new SimpleDateFormat("yy"); 
        final String formattedYear = df.format(Calendar.getInstance().getTime());
        
        //genero aleatorios para el id
        final Random r = new Random();
        final char c1 = (char) (r.nextInt(26) + 'a');
        
        final Random r2 = new Random();
        final char c2 = (char) (r2.nextInt(26) + 'a');
        
        final double randNumber = Math.random();
        final double d = randNumber * 100;

        final int randomInt = (int)d;
        
        entity.getPominok()
                .setMarker(formattedYear + (month.length() == 1 ? "0" : "") + month + (day.length() == 1 ? "0" : "") + day +"/" + randomInt +"/" +c1 +c2);
        this.repository.save(entity);
        this.repository.flush();

    }

}
