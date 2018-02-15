package tum.sebis.apm.repository;

import tum.sebis.apm.domain.Person;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    List<Person> findAllByOrderByName();
}
