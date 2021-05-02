
package acme.entities.workPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;
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

    protected static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------
    @NotBlank
    @Length(min = 1, max = 80)
    protected String title;

    @NotBlank
    @Size(min = 1, max = 500)
    protected String description;

    @NotNull
    protected Boolean isPublic;

    @NotNull
    @Valid
    protected ExecutionPeriod executionPeriod;

    // Derived attributes -----------------------------------------------------
    public Double getWorkloadHours() {
        return this.tasks.stream().collect(Collectors.summarizingDouble(x -> x.getWorkloadHours())).getSum();
    }

    @Transient
    List<String> newTasksId;

    public Boolean isFinished() {
        final Date now = new Date();
        return now.after(this.executionPeriod.getFinishDateTime());
    }
    // Relationships ----------------------------------------------------------

    @Valid
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    protected List<Task> tasks;

    @Valid
    @ManyToOne(optional = false)
    protected UserAccount user;

}