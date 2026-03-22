package turminha.BibliotecaDigital.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false)
    private LocalDate releaseDate;

    private List<Author> authors = new ArrayList<>();



}
