package acme.features.spam;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotSpamValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotSpamConstraint {
    String message() default "{acme.features.spam.isSpam}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}