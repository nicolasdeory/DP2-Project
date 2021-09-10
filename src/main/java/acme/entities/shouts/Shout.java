package acme.entities.shouts;

import java.util.Date;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import acme.entities.mocke.Mocke;
import acme.features.spam.NotSpamConstraint;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Shout extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	protected Date moment;

	@NotBlank
	@NotSpamConstraint
	@Length(min = 5, max = 25)
	protected String author;

	@NotBlank
	@Length(min = 1, max = 100)
	@NotSpamConstraint
	protected String text;

	@URL
	@NotSpamConstraint
	protected String info;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Shout))
			return false;
		if (!super.equals(o))
			return false;
		Shout shout = (Shout) o;
		return Objects.equals(moment, shout.moment) && Objects.equals(author, shout.author)
				&& Objects.equals(text, shout.text) && Objects.equals(info, shout.info);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), moment, author, text, info);
	}

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@OneToOne(optional = true, cascade = CascadeType.ALL)
	protected Mocke mocke;

}
