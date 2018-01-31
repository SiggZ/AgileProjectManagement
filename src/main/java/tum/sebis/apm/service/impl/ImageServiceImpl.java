package tum.sebis.apm.service.impl;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import tum.sebis.apm.domain.UserImage;
import tum.sebis.apm.domain.UserImageData;
import tum.sebis.apm.service.ImageService;
import tum.sebis.apm.web.rest.errors.ImageNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Images.
 */
@Service
public class ImageServiceImpl implements ImageService{

    private final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final GridFsTemplate gridFsTemplate;

    public ImageServiceImpl(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public GridFSFile save(UserImage image) {
        log.debug("Request to save image: {}", image);
        GridFSFile file = gridFsTemplate.store(new ByteArrayInputStream(image.getEncodedImage()), image.getName());
        return file;
    }

    @Override
    public GridFSDBFile findOneById(String id) {
        log.debug("Request to get image: {}", id);
        GridFSDBFile file = gridFsTemplate.findOne(
            new Query().addCriteria(Criteria.where("_id").is(id)));
        if (file == null) {
            throw new ImageNotFoundException();
        }
        return file;
    }

    @Override
    public GridFSDBFile findOneByName(String name) {
        log.debug("Request to get image: {}", name);
        GridFSDBFile file = gridFsTemplate.findOne(
            new Query().addCriteria(Criteria.where("filename").is(name)));
        return file;
    }

    @Override
    public List<UserImageData> findAll() {
        log.debug("Request to get all images");
        List<UserImageData> imageData = new ArrayList<>();
        gridFsTemplate.find(null).stream().forEach(file -> imageData.add(
            new UserImageData().imageId(file.getId().toString()).name(file.getFilename())));
        return imageData;
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete image : {}", id);
        // TODO: validation?
        gridFsTemplate.delete(
            new Query().addCriteria(Criteria.where("_id").is(id)));
    }

    @Override
    public GridFSFile savePlaceholderImage(String filename) {
        log.debug("Request to save placeholder user image");
        GridFSFile file = null;
        InputStream inputStream = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            inputStream = new FileInputStream(classLoader.getResource("images/" + filename).getFile());
            file = gridFsTemplate.store(inputStream, filename, "image/png", null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
