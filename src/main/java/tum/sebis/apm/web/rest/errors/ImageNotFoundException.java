package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ImageNotFoundException extends AbstractThrowableProblem {

    public ImageNotFoundException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.NOT_FOUND,
            ErrorConstants.IMAGE_NOT_FOUND_MESSAGE);
    }
}
