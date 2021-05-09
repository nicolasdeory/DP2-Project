
package acme.entities.workPlan;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import acme.datatypes.ExecutionPeriod;
import acme.entities.tasks.Task;
import acme.features.spam.NotSpamConstraint;
import acme.framework.entities.DomainEntity;
import acme.framework.entities.UserAccount;
import acme.utils.WorkLoadOperations;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WorkPlan extends DomainEntity {
    // Serialisation identifier -----------------------------------------------

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @NotBlank
    @NotSpamConstraint
    @Length(min = 5, max = 80)
    protected String title;

    @NotBlank
    @NotSpamConstraint
    @Length(min = 1, max = 500)
    protected String description;

    @NotNull
    protected Boolean isPublic;

    @NotNull
    @Valid
    protected ExecutionPeriod executionPeriod;

    // Derived attributes -----------------------------------------------------
    public Double getWorkloadHours() {
        return WorkLoadOperations.formatWorkload(
                this.tasks.stream().collect(Collectors.summarizingDouble(x -> WorkLoadOperations.unformatWorkload(x.getWorkload()))).getSum());
    }

    @Transient
    List<String> newTasksId;

    public Boolean isFinished() {
        final Date now = new Date();
        if (this.executionPeriod.getFinishDateTime() != null) {
            return now.after(this.executionPeriod.getFinishDateTime());
        } else {
            return null;
        }

    }
    // Relationships ----------------------------------------------------------

    @Valid
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private List<Task> tasks;

    @Valid
    @ManyToOne(optional = false)
    protected UserAccount user;

}
