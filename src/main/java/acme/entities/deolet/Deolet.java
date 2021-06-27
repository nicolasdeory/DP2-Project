package acme.entities.deolet;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import acme.entities.shouts.Shout;
import acme.framework.datatypes.Money;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Deolet extends DomainEntity {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @Pattern(regexp = "^(\\w{6}):(\\d{4}):(\\d{2})$")
    @Column(unique = true)
    protected String tracker;

    protected Date deadline;

    protected Money budget;

    @NotNull
    protected Boolean important;

    // Derived attributes -----------------------------------------------------

    // Relationships ----------------------------------------------------------
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    protected Shout shout;
}
