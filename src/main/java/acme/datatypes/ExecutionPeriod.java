/*
 * UserIdentity.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.datatypes;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.framework.datatypes.DomainDatatype;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@ToString
public class ExecutionPeriod extends DomainDatatype implements Comparable<ExecutionPeriod> {

	// Serialisation identifier -----------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------

	@Valid
	@NotNull
	protected Date startDateTime;

	@Valid
	@NotNull
	protected Date finishDateTime;

	// Derived attributes -----------------------------------------------------


	public Double getWorkloadHours() {
		return (this.finishDateTime.getTime() - this.startDateTime.getTime()) / (1000 * 3600.0);
	}

	@Override
	public int compareTo(ExecutionPeriod o) {
		double diff = this.getWorkloadHours() - o.getWorkloadHours();
		int aux = diff > 0 ? 1 : 0;
		return diff < 0 ? -1 : aux;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExecutionPeriod)) return false;
		ExecutionPeriod that = (ExecutionPeriod) o;
		return Objects.equals(startDateTime, that.startDateTime) &&
				Objects.equals(finishDateTime, that.finishDateTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startDateTime, finishDateTime);
	}
}
