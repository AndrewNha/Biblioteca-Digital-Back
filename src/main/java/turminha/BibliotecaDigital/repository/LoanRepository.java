package turminha.BibliotecaDigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import turminha.BibliotecaDigital.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {



}
