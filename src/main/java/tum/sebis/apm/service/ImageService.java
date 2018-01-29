package tum.sebis.apm.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import tum.sebis.apm.domain.UserImage;

/**
 * Service Interface for managing Images.
 */
public interface ImageService {

    /**
     * Save an image.
     *
     * @param image the entity to save
     * @return the persisted entity
     */
    GridFSFile save(UserImage image);

    /**
     *  Get an image by its id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GridFSDBFile findOne(String id);

    /**
     *  Delete the image with the given id.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}
