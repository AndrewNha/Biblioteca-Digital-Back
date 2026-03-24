package turminha.BibliotecaDigital.controller;

import org.springframework.web.bind.annotation.*;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.model.Loan;
import turminha.BibliotecaDigital.service.BookService;
import turminha.BibliotecaDigital.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    //Final -> pra ngm mudar dps o service
    //e fazer caquinha
    private final BookService bookService;

    public BookController(BookService service) {
        this.bookService = service;
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.save(book);
    }

    //PathVariable -> busca na URL
    //RequestBody -> corpo da requisição, json
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

}
