package acme.entities.XXX;

import acme.entities.shouts.Shout;
import acme.framework.datatypes.Money;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Currency;
import java.util.Date;


@Entity
@Getter
@Setter
public class XXX extends DomainEntity {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @Column(name = "Xdate", unique = true)
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-YYYY")
    protected Date Xdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Past
    @NotNull
    protected Date shoutMoment;


    protected Money currency;

    @NotNull
    protected Boolean XXXflag;



    // Derived attributes -----------------------------------------------------


    // Relationships ----------------------------------------------------------
    @OneToOne(optional = false)
    protected Shout shout;
}
