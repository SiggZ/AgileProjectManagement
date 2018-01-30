package tum.sebis.apm.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

/**
 *  Meta data which is stored with a person containing information about the person's user image.
 *  The user image itself is stored in a separate mongoDB collection.
 */
@Getter
@Setter
public class UserImageData {

    @NotNull
    @Field("image_id")
    private String imageId;

    @Field("name")
    private String name;

    public UserImageData imageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public UserImageData name(String name) {
        this.name = name;
        return this;
    }
}
