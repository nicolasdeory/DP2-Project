package acme.features.spam;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotSpamValidator implements ConstraintValidator<NotSpamConstraint, String> {

    SpamService spamService;

    @Autowired
    public NotSpamValidator(SpamService spamService)
    {
        this.spamService = spamService;
    }

    public NotSpamValidator()
    {

    }


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (spamService == null)
            return true;

        return !spamService.isStringSpam(s);
    }
}
