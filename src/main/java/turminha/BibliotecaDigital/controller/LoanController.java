package turminha.BibliotecaDigital.controller;

import org.springframework.web.bind.annotation.*;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.model.Loan;
import turminha.BibliotecaDigital.model.User;
import turminha.BibliotecaDigital.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService service) {
        this.loanService = service;
    }

    @GetMapping
    public List<Loan> getAll() {
        return loanService.getAll();
    }

    @GetMapping("/most-borrowed-book")
    public Book mostBorrowedBook() {
        return loanService.mostBorrowedBook();
    }

    @GetMapping("/most-active-user")
    public User mostActiveUser() {
        return loanService.mostActiveUser();
    }

    @PostMapping
    public Loan create(@RequestBody Loan loan) {
        return loanService.save(loan);
    }

    //Esse id é justamente o id doq eu quero
    //atualizar, deletar etc.
    //é logo na url;
    //Esse @PathVariable identifica que o id que é passado
    //No metodo é JUSTAMENTE o id da url
    @PutMapping("/{id}")
    public Loan update(@PathVariable Long id, @RequestBody Loan loan) {
        return loanService.update(id, loan);
    }

    //Essa anotação é para significar que é uma atualização parcial
    //(muda o status, ja q é devolução)
    @PatchMapping("/{id}/return")
    public Loan returnBook(@PathVariable Long id) {
        return loanService.returnBook(id);
    }

    //A unica responsabilidade dos controllers
    //é tankar o HTTP, por isso os
    //métodos tão meio vazios

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        loanService.delete(id);
    }
}
