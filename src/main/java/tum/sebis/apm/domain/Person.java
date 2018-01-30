package tum.sebis.apm.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Person.
 */
@Getter
@Setter
@Document(collection = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("surname")
    private String surname;

    @Field("location")
    private String location;

    @Field("grade")
    private String grade;

    @Field("project_availability")
    private Double projectAvailability;

    @Field("sprint_availability")
    private Double sprintAvailability;

    @Field("ahcr")
    private Double ahcr;

    @Valid
    @Field("user_image_data")
    private UserImageData userImageData;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Person name(String name) {
        this.name = name;
        return this;
    }

    public Person surname(String surname) {
        this.surname = surname;
        return this;
    }

    public Person location(String location) {
        this.location = location;
        return this;
    }

    public Person grade(String grade) {
        this.grade = grade;
        return this;
    }

    public Person projectAvailability(Double projectAvailability) {
        this.projectAvailability = projectAvailability;
        return this;
    }

    public Person ahcr(Double ahcr) {
        this.ahcr = ahcr;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        if (person.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), person.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", location='" + getLocation() + "'" +
            ", grade='" + getGrade() + "'" +
            ", projectAvailability='" + getProjectAvailability() + "'" +
            ", sprintAvailability='" + getSprintAvailability() + "'" +
            ", ahcr='" + getAhcr() + "'" +
            ", userImage='" + getUserImageData() + "'" +
            "}";
    }
}
