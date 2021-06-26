package acme.entities.shouts;


import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.deolet.Deolet;
import acme.features.spam.NotSpamConstraint;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Shout extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	protected Date				moment;

	@NotBlank
	@NotSpamConstraint
	@Length(min = 5, max = 25)
	protected String			author;

	@NotBlank
	@Length(min = 1, max = 100)
	@NotSpamConstraint
	protected String			text;
	
	@URL
	@NotSpamConstraint
	protected String			info;

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Shout)) return false;
		if (!super.equals(o)) return false;
		final Shout shout = (Shout) o;
		return Objects.equals(this.moment, shout.moment) &&
				Objects.equals(this.author, shout.author) &&
				Objects.equals(this.text, shout.text) &&
				Objects.equals(this.info, shout.info);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.moment, this.author, this.text, this.info);
	}

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@OneToOne(optional = true,cascade = CascadeType.ALL)
	protected Deolet deolet;

}
