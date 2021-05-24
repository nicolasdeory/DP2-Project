/*
 * Authenticated.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.spam;

import java.util.Collection;
import java.util.Objects;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpamParameters extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------

	@Range(min = 0, max = 1)
	protected Double threshold;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Collection<String> keywords;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------


	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof SpamParameters)) return false;
		if (!super.equals(o)) return false;
		final SpamParameters that = (SpamParameters) o;
		return Objects.equals(this.threshold, that.threshold) &&
				Objects.equals(this.keywords, that.keywords);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.threshold, this.keywords);
	}


}
