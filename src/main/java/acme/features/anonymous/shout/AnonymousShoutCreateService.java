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
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.nft.Nft;
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
        request.bind(entity.getNft(), errors);

    }

    @Override
    public void unbind(final Request<Shout> request, final Shout entity, final Model model) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertModelNotNull(model);

        request.unbind(entity, model, "author", "text", "info");
        request.unbind(entity.getNft(), model, "idNft", "salary",
                "ignoreNft", "lineDead");
    }

    @Override
    public Shout instantiate(final Request<Shout> request) {
        AssertUtils.assertRequestNotNull(request);

        Shout result;
        Date moment;

        moment = new Date(System.currentTimeMillis() - 1);

        result = new Shout();
        result.setMoment(moment);
        result.setNft(new Nft());

        return result;
    }

    @Override
    public void validate(final Request<Shout> request, final Shout entity, final Errors errors) {
        AssertUtils.assertRequestNotNull(request);
        AssertUtils.assertEntityNotNull(entity);
        AssertUtils.assertErrorsNotNull(errors);
        final Nft nft = entity.getNft();

        if(nft.getLineDead()==null){
            errors.state(request,false,"lineDead","anonymous.shout.nft.error.lineDead.null");
        }

        if (nft.getSalary() != null
                && (!(nft.getSalary().getCurrency().equals("EUR")
                        || nft.getSalary().getCurrency().equals("USD")|| nft.getSalary().getCurrency().equals("GBP")))) {
            errors.state(request, false, "salary",
                    "anonymous.shout.nft.error.salary.format");
        }

        if (nft.getSalary() != null
                && nft.getSalary().getAmount() < 0) {
            errors.state(request, false, "salary",
                    "anonymous.shout.nft.error.salary.negative");
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.WEEK_OF_YEAR, +1);
        if (nft.getLineDead() != null
                && nft.getLineDead().before(c.getTime())) {
            errors.state(request, false, "lineDead",
                    "anonymous.shout.nft.error.lineDead.week");
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

        entity.getNft().setShout(entity);

        // Estrategia para generar un identificador aleatorio. Escogemos un número aleatorio basado en la hora del sistema
        // y la fecha. Lo adaptamos al patrón que nos piden.
        String randInt = String.valueOf(System.currentTimeMillis());

        // TODO: Borrar esto si no piden generar letras. Acordarse!!!
        // Transformamos los números a letras para conformarse al patron

        //genero aleatorios para el id
        final Random r = new Random();
        final char c1 = (char) (r.nextInt(26) + 'a');

        final Random r2 = new Random();
        final char c2 = (char) (r2.nextInt(26) + 'a');



        entity.getNft().setIdNft(year.substring(2)+":"+c1+c2+
    ":"+ (month.length() == 1 ? "0" : "") + month+":"+randInt.substring(randInt.length() - 5) + ":" +(day.length() == 1 ? "0" : "") + day);
        this.repository.save(entity);

    }

}
