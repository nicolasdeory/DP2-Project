package acme.entities.roles;

import acme.framework.entities.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class Manager extends UserRole {
    // Serialisation identifier -----------------------------------------------

    protected static final long	serialVersionUID	= 1L;

    // Attributes -------------------------------------------------------------

    @NotBlank
    protected String			team;


    // Derived attributes -----------------------------------------------------

    // Relationships ----------------------------------------------------------
}
