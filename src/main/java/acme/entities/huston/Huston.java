package acme.entities.huston;

import acme.entities.shouts.Shout;
import acme.framework.datatypes.Money;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Getter
@Setter
public class Huston extends DomainEntity {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    protected String identifier;

    protected Date deadline;

    protected Money budget;

    @NotNull
    protected Boolean important;

    // Derived attributes -----------------------------------------------------

    // Relationships ----------------------------------------------------------
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    protected Shout shout;
}
