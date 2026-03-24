package turminha.BibliotecaDigital.service;

import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.model.Author;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //LISTAR TODOS
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    //CRIAR
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    //DELETAR
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found."));
        bookRepository.delete(book);
    }

    //UPDATE
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
