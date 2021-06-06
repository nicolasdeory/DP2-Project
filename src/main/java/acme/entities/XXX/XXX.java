package acme.entities.XXX;

import acme.entities.shouts.Shout;
import acme.framework.datatypes.Money;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Date;


@Entity
@Getter
@Setter
public class XXX extends DomainEntity {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @Column(unique = true)
    protected Date Xdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Past
    protected Date shoutMoment;


    protected Money currency;

    @NotNull
    protected Boolean XXXflag;



    // Derived attributes -----------------------------------------------------
    @Transient
    protected String XdateString;

    // Relationships ----------------------------------------------------------
    @OneToOne(optional = false,cascade = CascadeType.ALL)
    protected Shout shout;
}
