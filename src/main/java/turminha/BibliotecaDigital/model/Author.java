package turminha.BibliotecaDigital.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("AUTHOR")
public class Author extends Person {

    @Column(nullable = false)
    private String nationality;

    @ManyToMany(mappedBy = "authors")
    private List<Book> booksWritten = new ArrayList<>();

    @Override
    public String getInfo() {
        return "Id: " + super.getId() + " / " + "Name: " + super.getName() + " / "
                + "Nationality: " + this.nationality;
    }

    public Author(){

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


