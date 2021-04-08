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

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
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

	@Future
	@NotNull
	protected Date startDateTime;

	@Future
	@NotNull
	protected Date finishDateTime;

	// Derived attributes -----------------------------------------------------

	@Transient
	public Double getWorkloadHours() {
		return (this.finishDateTime.getTime() - this.startDateTime.getTime()) / (1000 * 3600.0);
	}

	@Override
	public int compareTo(ExecutionPeriod o) {
		double diff = this.getWorkloadHours() - o.getWorkloadHours();
		return diff < 0 ? -1 : (diff > 0 ? 1 : 0);
	}
}
