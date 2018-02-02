package tum.sebis.apm.service.impl;

import com.mongodb.gridfs.GridFSDBFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tum.sebis.apm.domain.Person;
import tum.sebis.apm.domain.UserImageData;
import tum.sebis.apm.repository.PersonRepository;
import tum.sebis.apm.service.ImageService;
import tum.sebis.apm.service.PersonService;

import java.util.List;

/**
 * Service Implementation for managing Person.
 */
@Service
public class PersonServiceImpl implements PersonService{

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;
    private final ImageService imageService;

    public PersonServiceImpl(PersonRepository personRepository, ImageService imageService) {
        this.personRepository = personRepository;
        this.imageService = imageService;
    }

    /**
     * Save a person.
     *
     * @param person the entity to save
     * @return the persisted entity
     */
    @Override
    public Person save(Person person) {
        log.debug("Request to save Person : {}", person);
        if (person.getProjectAvailability() == null) {
            person.setProjectAvailability(0.0);
        }
        if (person.getSprintAvailability() == null) {
            person.setSprintAvailability(0.0);
        }
        person.setUserImageData(retrieveUserImageData(person.getUserImageData()));
        return personRepository.save(person);
    }

    /**
     *  Get all the people.
     *
     *  @return the list of entities
     */
    @Override
    public List<Person> findAll() {
        log.debug("Request to get all People");
        return personRepository.findAll();
    }

    /**
     *  Get one person by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public Person findOne(String id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findOne(id);
    }

    /**
     *  Delete the  person by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.delete(id);
    }

    /**
     *  Finds and returns user image data. If an id is provided, the image with the id is queried from the image service.
     *  Otherwise, an image with the default placeholder image name is queried. If it could not be found in the db,
     *  null is returned.
     *
     * @param data the user image data provided in the request
     * @return user image data or null
     */
    private UserImageData retrieveUserImageData(UserImageData data) {
        GridFSDBFile file;
        if (data == null) {
            file = imageService.findOneByName("placeholder_user_image.png");
        } else {
            file = imageService.findOneById(data.getImageId());
        }
        if (file != null) {
            return new UserImageData().imageId(file.getId().toString()).name(file.getFilename());
        }
        return null;
    }
}
