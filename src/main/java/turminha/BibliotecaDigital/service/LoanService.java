package turminha.BibliotecaDigital.service;


import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.model.Loan;
import turminha.BibliotecaDigital.repository.LoanRepository;

import java.util.List;

@Service
public class LoanService {

    private LoanRepository loanRepository;

    public LoanService(LoanRepository repository) {
        this.loanRepository = repository;
    }

    //LISTAR TODOS
    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    //CRIAR
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    //DELETAR
    public void delete(Long id) {
        loanRepository.deleteById(id);
    }




}
