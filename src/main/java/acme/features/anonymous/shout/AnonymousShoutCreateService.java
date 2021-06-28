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

import acme.entities.Iowe.Iowe;
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
        request.bind(entity.getIowe(), errors);

    }

    @Override
    public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "author", "text", "info");
        request.unbind(entity.getIowe(), model, "deadline", "budget", "important");
    }

    @Override
    public Shout instantiate(final Request<Shout> request) {
        AssertUtils.assertRequestNotNull(request);

        Shout result;
        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);

        result = new Shout();
        result.setMoment(moment);
        result.setIowe(new Iowe());

        return result;
    }

    @Override
    public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        Iowe iowe = entity.getIowe();
        if (iowe.getBudget() != null
                && (!(iowe.getBudget().getCurrency().equals("EUR") || iowe.getBudget().getCurrency().equals("USD")))) {
            errors.state(request, false, "budget", "anonymous.shout.Iowe.error.budget.format");
        }

        if (iowe.getBudget() != null && iowe.getBudget().getAmount() < 0)
        {
            errors.state(request, false, "budget", "anonymous.shout.Iowe.error.budget.negative");
        }


        if (iowe.getDeadline() != null)
        {
            Date today = new Date(System.currentTimeMillis() - 1);
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.WEEK_OF_MONTH, 1);

            if (!iowe.getDeadline().after(c.getTime()))
                errors.state(request, false, "deadline", "anonymous.shout.Iowe.error.deadline.tooearly");
        }

    }

    @Override
    public void create(final Request<Shout> request, final Shout entity) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);

        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());

        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);
        entity.setMoment(moment);
//        Date Xdate = new Date(System.currentTimeMillis() - 1);
//        Calendar c = Calendar.getInstance();
//        c.setTime(Xdate);
//        c.add(Calendar.MONTH, -1);
//        entity.getIowe().setDeadline(c.getTime());
        entity.getIowe().setShout(entity);

        entity.getIowe()
                .setIdentifier(year.substring(2,4) + (month.length() == 1 ? "0" : "") + month + (day.length() == 1 ? "0" : "") + day + "#000");
        this.repository.save(entity);
        this.repository.flush();
        Integer id = entity.getId();
        if (id > 999)
            id = 999;
        entity.getIowe().setIdentifier(entity.getIowe().getIdentifier().substring(0, entity.getIowe().getIdentifier().length() - 3) + padLeftZeros(id.toString(),3));
        this.repository.save(entity);
    }

    String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

}
