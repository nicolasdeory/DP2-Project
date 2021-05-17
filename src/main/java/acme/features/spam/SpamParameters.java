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

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class SpamParameters extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------

	protected Double threshold;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Collection<String> keywords;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SpamParameters)) return false;
		if (!super.equals(o)) return false;
		SpamParameters that = (SpamParameters) o;
		return Objects.equals(threshold, that.threshold) &&
				Objects.equals(keywords, that.keywords);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), threshold, keywords);
	}


}
