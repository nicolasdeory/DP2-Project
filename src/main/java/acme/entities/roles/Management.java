package acme.entities.roles;

import acme.features.spam.NotSpamConstraint;
import acme.framework.entities.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Getter
@Setter
public class Management extends UserRole {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Management that = (Management) o;
        return Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), team);
    }

    @NotBlank
    @Length(min = 1, max = 50)
    @NotSpamConstraint
    protected String team;

    // Derived attributes -----------------------------------------------------

    // Relationships ----------------------------------------------------------
}
