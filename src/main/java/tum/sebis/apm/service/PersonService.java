package tum.sebis.apm.service;

import tum.sebis.apm.domain.Person;
import java.util.List;

/**
 * Service Interface for managing Person.
 */
public interface PersonService {

    /**
     * Save a person.
     *
     * @param person the entity to save
     * @return the persisted entity
     */
    Person save(Person person);

    /**
     *  Get all the people.
     *
     *  @return the list of entities
     */
    List<Person> findAll();

    /**
     *  Get the "id" person.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Person findOne(String id);

    /**
     *  Delete the "id" person.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}
