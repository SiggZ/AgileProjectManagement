package tum.sebis.apm.web.rest;

import tum.sebis.apm.PersonServiceApp;

import tum.sebis.apm.domain.Person;
import tum.sebis.apm.repository.PersonRepository;
import tum.sebis.apm.service.PersonService;
import tum.sebis.apm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static tum.sebis.apm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PersonResource REST controller.
 *
 * @see PersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonServiceApp.class)
public class PersonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    private static final Double DEFAULT_PROJECT_AVAILABILITY = 1D;
    private static final Double UPDATED_PROJECT_AVAILABILITY = 2D;

    private static final Double DEFAULT_AHCR = 1D;
    private static final Double UPDATED_AHCR = 2D;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restPersonMockMvc;

    private Person person;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersonResource personResource = new PersonResource(personService);
        this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity() {
        Person person = new Person()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .location(DEFAULT_LOCATION)
            .grade(DEFAULT_GRADE)
            .projectAvailability(DEFAULT_PROJECT_AVAILABILITY)
            .ahcr(DEFAULT_AHCR);
        return person;
    }

    @Before
    public void initTest() {
        personRepository.deleteAll();
        person = createEntity();
    }

    @Test
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person
        restPersonMockMvc.perform(post("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPerson.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testPerson.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPerson.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testPerson.getProjectAvailability()).isEqualTo(DEFAULT_PROJECT_AVAILABILITY);
        assertThat(testPerson.getAhcr()).isEqualTo(DEFAULT_AHCR);
    }

    @Test
    public void createPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person with an existing ID
        person.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc.perform(post("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setName(null);

        // Create the Person, which fails.

        restPersonMockMvc.perform(post("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setSurname(null);

        // Create the Person, which fails.

        restPersonMockMvc.perform(post("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.save(person);

        // Get all the personList
        restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.toString())))
            .andExpect(jsonPath("$.[*].projectAvailability").value(hasItem(DEFAULT_PROJECT_AVAILABILITY.doubleValue())))
            .andExpect(jsonPath("$.[*].ahcr").value(hasItem(DEFAULT_AHCR.doubleValue())));
    }

    @Test
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.save(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.toString()))
            .andExpect(jsonPath("$.projectAvailability").value(DEFAULT_PROJECT_AVAILABILITY.doubleValue()))
            .andExpect(jsonPath("$.ahcr").value(DEFAULT_AHCR.doubleValue()));
    }

    @Test
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePerson() throws Exception {
        // Initialize the database
        personService.save(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findOne(person.getId());
        updatedPerson
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .location(UPDATED_LOCATION)
            .grade(UPDATED_GRADE)
            .projectAvailability(UPDATED_PROJECT_AVAILABILITY)
            .ahcr(UPDATED_AHCR);

        restPersonMockMvc.perform(put("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPerson)))
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPerson.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testPerson.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPerson.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testPerson.getProjectAvailability()).isEqualTo(UPDATED_PROJECT_AVAILABILITY);
        assertThat(testPerson.getAhcr()).isEqualTo(UPDATED_AHCR);
    }

    @Test
    public void updateNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Create the Person

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersonMockMvc.perform(put("/api/people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deletePerson() throws Exception {
        // Initialize the database
        personService.save(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Get the person
        restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Person.class);
        Person person1 = new Person();
        person1.setId("id1");
        Person person2 = new Person();
        person2.setId(person1.getId());
        assertThat(person1).isEqualTo(person2);
        person2.setId("id2");
        assertThat(person1).isNotEqualTo(person2);
        person1.setId(null);
        assertThat(person1).isNotEqualTo(person2);
    }
}
