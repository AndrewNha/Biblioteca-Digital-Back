package turminha.BibliotecaDigital.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.enums.LoanStatus;
import turminha.BibliotecaDigital.enums.ReservationStatus;
import turminha.BibliotecaDigital.model.Author;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.repository.AuthorRepository;
import turminha.BibliotecaDigital.repository.BookRepository;
import turminha.BibliotecaDigital.repository.LoanRepository;
import turminha.BibliotecaDigital.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, LoanRepository loanRepository, ReservationRepository reservationRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
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

        long activeLoans = loanRepository.countByBookAndStatus(book, LoanStatus.ACTIVE)
                + loanRepository.countByBookAndStatus(book, LoanStatus.LATE);

        if (activeLoans > 0) {
            throw new RuntimeException("Cannot delete book with active loans.");
        }

        if (reservationRepository.existsByBookAndStatus(book, ReservationStatus.PENDING)) {
            throw new RuntimeException("Cannot delete book with pending reservations.");
        }

        if (!book.getAuthors().isEmpty()) {
            throw new RuntimeException("Cannot delete book with authors associated. Remove the authors from the book first.");
        }

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

        //É BUG Q N ACABA MAIS CARA, EU NUNCA MAIS VOU PROGRAMAR MDS KK É MEME PROF :)
        if (bookUpdated.getAuthors() != null && !bookUpdated.getAuthors().isEmpty()) {
            List<Author> authors = new ArrayList<>();

            for (Author nullAuthor : bookUpdated.getAuthors()) {
                Author author = authorRepository.findById(nullAuthor.getId())
                        .orElseThrow(() -> new RuntimeException("Author not found with ID: " + nullAuthor.getId()));

                authors.add(author);
            }

            book.setAuthors(authors);

        } else {
            book.setAuthors(new ArrayList<>());
        }

        return bookRepository.save(book);
    }
}
