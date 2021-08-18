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
import java.util.Formatter;
import java.util.Random;

import acme.entities.huston.Huston;
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
        request.bind(entity.getHuston(), errors);

    }

    @Override
    public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "author", "text", "info");
        request.unbind(entity.getHuston(), model, "identifier", "budget", "important","deadline");
    }

    @Override
    public Shout instantiate(final Request<Shout> request) {
        AssertUtils.assertRequestNotNull(request);

        Shout result;
        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);

        result = new Shout();
        result.setMoment(moment);
        result.setHuston(new Huston());

        return result;
    }

    @Override
    public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        Huston huston = entity.getHuston();
        if (huston.getBudget() != null
                && (!(huston.getBudget().getCurrency().equals("EUR") || huston.getBudget().getCurrency().equals("USD")|| huston.getBudget().getCurrency().equals("GBP")))) {
            errors.state(request, false, "budget", "anonymous.shout.huston.error.budget.format");
        }

        if (huston.getBudget() != null && huston.getBudget().getAmount() <= 0)
        {
            errors.state(request, false, "budget", "anonymous.shout.huston.error.budget.negative");
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.WEEK_OF_YEAR, +1);
        if(huston.getDeadline()!=null&&huston.getDeadline().before(c.getTime())){
            errors.state(request, false, "deadline", "anonymous.shout.huston.error.deadline.week");

        }



    }

    @Override
    public void create(final Request<Shout> request, final Shout entity) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);

        Date moment;

        Random random = new Random();
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear()).substring(2);
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());
        if(day.length()==1) day="0"+day;
        if(month.length()==1) month="0"+month;
        Boolean hustonDuplicated=true;
        while (hustonDuplicated){
            String identifier=random.nextInt(999999)+":"+year+":"+month+":"+day;
            entity.getHuston().setIdentifier(identifier);
            hustonDuplicated=repository.hasDuplicatedIdentifier(entity.getHuston().getIdentifier())!=null;
        }

        moment = new Date(System.currentTimeMillis() - 1);
        entity.setMoment(moment);
        entity.getHuston().setShout(entity);


        this.repository.save(entity);
    }

}
