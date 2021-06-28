package acme.entities.Iowe;

import acme.entities.shouts.Shout;
import acme.framework.datatypes.Money;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Iowe extends DomainEntity {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @Pattern(regexp = "^(\\d{2})(\\d{2})(\\d{2})#\\w{3}$")
    @Column(unique = true)
    protected String identifier;

    @NotNull
    protected Date deadline;

    @NotNull
    protected Money budget;

    @NotNull
    protected Boolean important;

    // Derived attributes -----------------------------------------------------

    // Relationships ----------------------------------------------------------
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    protected Shout shout;
}
