package tum.sebis.apm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tum.sebis.apm.domain.UserImage;
import tum.sebis.apm.service.ImageService;
import tum.sebis.apm.web.rest.errors.BadRequestAlertException;
import tum.sebis.apm.web.rest.errors.InternalServerErrorException;
import tum.sebis.apm.web.rest.util.HeaderUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for managing images.
 */
@RestController
@RequestMapping("/api")
public class ImageResource {

    private final Logger log = LoggerFactory.getLogger(ImageResource.class);

    private static final String ENTITY_NAME = "image";

    private final ImageService imageService;

    public ImageResource(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * POST  /people : Save a new image.
     *
     * @param image the image to save
     * @return the ResponseEntity with status 201 (Created) and with body the image
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/images")
    @Timed
    public ResponseEntity<UserImage> saveImage(@Valid @RequestBody UserImage image) throws URISyntaxException {
        log.debug("REST request to save image : {}", image.getName());
        try
        {
            GridFSFile file = imageService.save(image);
            return ResponseEntity.created(new URI("/api/images/" + file.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, file.getFilename()))
                .body(image);
        }
        catch(Exception e)
        {
            throw new BadRequestAlertException(e.getMessage(), null, null);
        }
    }

    /**
     * GET  /images/:id : get the image with the given id.
     *
     * @param id the id of the image to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the image, or with status 404 (Not Found)
     */
    @GetMapping("/images/{id}")
    @Timed
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        log.debug("REST request to get image : {}", id);
        GridFSDBFile file = imageService.findOne(id);
        try {
            return ResponseEntity.ok()
                .contentLength(file.getLength())
                .contentType(MediaType.parseMediaType("image/png"))
                .body(IOUtils.toByteArray(file.getInputStream()));
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * DELETE  /images/:id : delete the image with the given id.
     *
     * @param id the id of the image to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/images/{id}")
    @Timed
    public ResponseEntity<Void> deleteImage(@PathVariable String id) {
        log.debug("REST request to delete image : {}", id);
        imageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
