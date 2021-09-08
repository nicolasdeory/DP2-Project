
package acme.entities.tasks;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

import acme.features.spam.NotSpamConstraint;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.datatypes.ExecutionPeriod;
import acme.entities.workPlan.WorkPlan;
import acme.framework.entities.DomainEntity;
import acme.framework.entities.UserAccount;
import acme.utils.WorkLoadOperations;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends DomainEntity {

    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @NotNull
    @NotEmpty
    @NotBlank
    @NotSpamConstraint
    @Length(min = 1, max = 80)
    protected String title;

    @NotNull
    protected Boolean isPublic;

    /*
     * Workload Format: X.YYY where: X is the number of hours YYY is the number of
     * minutes
     * 
     * The requested max for hours is 99 and for minutes is 59
     */
    @NotNull
    @DecimalMax("99.59")
    @DecimalMin("0.00")
    protected Double workload;

    @NotNull
    @Valid
    protected ExecutionPeriod executionPeriod;

    @NotNull
    @NotEmpty
    @NotBlank
    @NotSpamConstraint
    @Length(min = 1, max = 500)
    protected String description;

    @URL
    @NotSpamConstraint
    protected String link;

    // Derived attributes -----------------------------------------------------

    @Transient
    public Boolean isFinished() {
        final Date now = new Date();
        return now.after(this.executionPeriod.getFinishDateTime());
    }

    // Relationships ----------------------------------------------------------
    @Valid
    @ManyToOne
    protected UserAccount user;

    @Valid
    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    protected List<WorkPlan> workPlans;
}
