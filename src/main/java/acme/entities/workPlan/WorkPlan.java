
package acme.entities.workPlan;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import acme.datatypes.ExecutionPeriod;
import acme.entities.tasks.Task;
import acme.framework.entities.DomainEntity;
import acme.framework.entities.UserAccount;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WorkPlan extends DomainEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Length(min = 5, max = 80)
	protected String			title;

	@NotBlank
	@Size(min = 1, max = 500)
	protected String			description;

	@NotNull
	protected Boolean			isPublic;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Boolean isFinished() {
		final Date now = new Date();
		return now.after(this.executionPeriod.getFinishDateTime());
	}


	@NotNull
	@Valid
	protected ExecutionPeriod	executionPeriod;

	// Relationships ----------------------------------------------------------

	@Valid
	@ManyToMany
	protected List<Task>		tasks;

	@Valid
	@ManyToOne
	protected UserAccount		user;

}
