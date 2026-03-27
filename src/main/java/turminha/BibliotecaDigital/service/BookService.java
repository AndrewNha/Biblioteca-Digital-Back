package turminha.BibliotecaDigital.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.model.Author;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.repository.AuthorRepository;
import turminha.BibliotecaDigital.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    //LISTAR TODOS
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    //LISTAR UM
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found."));
    }

    //CRIAR
    @Transactional
    public Book save(Book book) {
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            List<Author> realAuthors = new ArrayList<>();

            for (Author nullAuthor : book.getAuthors()) {

                Author realAuthor = authorRepository.findById(nullAuthor.getId())
                        .orElseThrow(() -> new RuntimeException("Author not found with ID: " + nullAuthor.getId()));

                realAuthor.getBooksWritten().add(book);

                realAuthors.add(realAuthor);

            }

            book.setAuthors(realAuthors);
        }

        return bookRepository.save(book);
    }

    //DELETAR
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found."));
        bookRepository.delete(book);
    }

    //UPDATE
    @Transactional
    public Book update(Long id, Book bookUpdated) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found."));

        book.setName(bookUpdated.getName());
        book.setGenre(bookUpdated.getGenre());
        book.setPublisher(bookUpdated.getPublisher());
        book.setReleaseDate(bookUpdated.getReleaseDate());
        book.setQuantity(bookUpdated.getQuantity());
        book.setQuantityAvailable(bookUpdated.getQuantityAvailable());
        book.setAuthors(bookUpdated.getAuthors());

        return bookRepository.save(book);
    }
}
