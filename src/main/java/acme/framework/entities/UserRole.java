/*
 * UserRole.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.framework.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import acme.datatypes.UserIdentity;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public abstract class UserRole extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------


	public GrantedAuthority getAuthority() {
		GrantedAuthority result;
		String authority;

		authority = String.format("AUTH_%s", this.getClass().getSimpleName());
		result = new SimpleGrantedAuthority(authority);

		return result;
	}

	public String getAuthorityName() {
		String result;

		result = this.getClass().getSimpleName();

		return result;
	}

	public UserIdentity getIdentity() {
		UserIdentity result;

		result = this.userAccount.getIdentity();

		return result;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne
	protected UserAccount userAccount;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserRole)) return false;
		if (!super.equals(o)) return false;
		UserRole userRole = (UserRole) o;
		return Objects.equals(userAccount, userRole.userAccount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), userAccount);
	}
}
