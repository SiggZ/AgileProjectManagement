package tum.sebis.apm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Person.
 */
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

    @Field("ahcr")
    private Double ahcr;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Person name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Person surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLocation() {
        return location;
    }

    public Person location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGrade() {
        return grade;
    }

    public Person grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Double getProjectAvailability() {
        return projectAvailability;
    }

    public Person projectAvailability(Double projectAvailability) {
        this.projectAvailability = projectAvailability;
        return this;
    }

    public void setProjectAvailability(Double projectAvailability) {
        this.projectAvailability = projectAvailability;
    }

    public Double getAhcr() {
        return ahcr;
    }

    public Person ahcr(Double ahcr) {
        this.ahcr = ahcr;
        return this;
    }

    public void setAhcr(Double ahcr) {
        this.ahcr = ahcr;
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
            ", ahcr='" + getAhcr() + "'" +
            "}";
    }
}
