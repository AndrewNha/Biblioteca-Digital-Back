package turminha.BibliotecaDigital.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends Person {

    @Column(nullable = false)
    private String nationality;

    @Override
    public String getInfo() {
        return "Id: " + super.getId() + " / " + "Name: " + super.getName() + " / "
                + "Nationality: " + this.nationality;
    }

    public Author(Long id, String name, String nationality) {
        super(id, name);
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}


