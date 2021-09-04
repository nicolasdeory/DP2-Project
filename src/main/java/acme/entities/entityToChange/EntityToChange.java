package acme.entities.entityToChange;

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
public class EntityToChange extends DomainEntity {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @Pattern(regexp = "^(\\d{6}):(\\d{4}):(\\d{2}):(\\d{2})$")
    @Column(unique = true)
    protected String idAttributeToChange;

    protected Date dateAttributeToChange;

    protected Money moneyAttributeToChange;

    @NotNull
    protected Boolean flagAttributeToChange;

    // Derived attributes -----------------------------------------------------

    // Relationships ----------------------------------------------------------
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    protected Shout shout;
}
