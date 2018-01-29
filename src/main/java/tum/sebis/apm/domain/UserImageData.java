package tum.sebis.apm.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

/**
 *  Meta data which is stored with a person containing information about the person's user image.
 *  The user image itself is stored in a separate mongoDB collection.
 */
public class UserImageData {

    @NotNull
    @Field("imageId")
    private String imageId;

    @NotNull
    @Field("name")
    private String name;
}
