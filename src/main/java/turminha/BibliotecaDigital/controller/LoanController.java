package turminha.BibliotecaDigital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import turminha.BibliotecaDigital.model.Loan;
import turminha.BibliotecaDigital.service.LoanService;

import java.time.LocalDate;
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

    @PostMapping
    public Loan create(@RequestBody Loan loan) {
        return loanService.save(loan);
    }
}
