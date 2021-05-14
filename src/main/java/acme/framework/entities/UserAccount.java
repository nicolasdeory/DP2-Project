/*
 * UserAccount.java
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

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import acme.datatypes.UserIdentity;
import acme.entities.tasks.Task;
import acme.entities.workplan.WorkPlan;
import acme.framework.helpers.PasswordHelper;
import acme.framework.helpers.StringHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserAccount extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(min = 5, max = 60)
	@Column(unique = true)
	protected String			username;

	@NotBlank
	@Length(min = 5, max = 60)
	protected String			password;


	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		if (password == null || password.equals("") || PasswordHelper.isEncoded(password))
			throw new IllegalArgumentException("Provide a valid password");

		this.password = PasswordHelper.encode(password);
	}


	protected boolean		enabled;

	@Valid
	protected UserIdentity	identity;

	// Derived attributes -----------------------------------------------------


	@Transient
	public boolean isAnonymous() {
		boolean result;

		result = this.username.equals("anonymous");

		return result;
	}

	// Relationships ----------------------------------------------------------


	@NotEmpty
	@OneToMany(mappedBy = "userAccount")
	private Collection<@Valid UserRole> roles;
	
	@Valid
	@OneToMany
	private List<Task> tasks;
	
	@Valid
	@OneToMany
	private List<WorkPlan> workPlans;

	@Transient
	public boolean hasRole(@NonNull final UserRole role) {
		return this.roles.contains(role);
	}

	@Transient
	public boolean hasRole(final Class<? extends UserRole> clazz) {
		return this.roles != null && this.getRole(clazz) != null;
	}

	@Transient
	@SuppressWarnings("unchecked")
	public <T extends UserRole> T getRole(final Class<? extends UserRole> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("class cannot be null");

		T result;
		Iterator<UserRole> iterator;
		UserRole role;

		result = null;
		if (this.roles != null) {
			iterator = this.roles.iterator();
			while (result == null && iterator.hasNext()) {
				role = iterator.next();
				result = role.getClass().equals(clazz) ? (T) role : null;
			}
		}

		return result;
	}

	@Transient
	@SuppressWarnings("unchecked")
	public <T extends UserRole> T getRole(final String name) {
		if (StringHelper.isBlank(name))
			throw new IllegalArgumentException("name cannot be blank");

		T result;
		Iterator<UserRole> iterator;
		UserRole role;

		result = null;
		if (this.roles != null) {
			iterator = this.roles.iterator();
			while (result == null && iterator.hasNext()) {
				role = iterator.next();
				result = role.getAuthorityName().equals(name) ? (T) role : null;
			}
		}

		return result;
	}

	public void addRole(final UserRole role) {
		if (role == null)
			throw new IllegalArgumentException("role cannot be null");
		if (this.hasRole(role.getClass()))
			throw new IllegalArgumentException("user already has role");

		if (this.roles == null) {
			this.roles = new ArrayList<>();
		}

		this.roles.add(role);
	}

	public void removeRole(final UserRole role) {
		if (role == null)
			throw new IllegalArgumentException("Role can't be null");
		if (!this.hasRole(role))
			throw new IllegalArgumentException("User doesn't have role");

		this.roles.remove(role);
	}

	// Other methods ----------------------------------------------------------

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserAccount)) return false;
		if (!super.equals(o)) return false;
		UserAccount that = (UserAccount) o;
		return enabled == that.enabled &&
				Objects.equals(username, that.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), username);
	}
}
