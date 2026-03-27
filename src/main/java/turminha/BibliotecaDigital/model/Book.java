package turminha.BibliotecaDigital.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonPropertyOrder({"id"})
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
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer quantityAvailable;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();


    public Book(){

    }

    public Book(String name, String genre, String publisher, LocalDate releaseDate, Integer quantity, Integer quantityAvailable, List<Author> authors) {
        this.name = name;
        this.genre = genre;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.quantity = quantity;
        this.quantityAvailable = quantityAvailable;
        this.authors = authors;
    }

    public Book(Long id, String name, String genre, String publisher, LocalDate releaseDate, Integer quantity, Integer quantityAvailable, List<Author> authors) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.quantity = quantity;
        this.quantityAvailable = quantityAvailable;
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
