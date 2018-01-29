package tum.sebis.apm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 *  An image with a name.
 */
@Getter
@Setter
public class UserImage {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] encodedImage;
}
