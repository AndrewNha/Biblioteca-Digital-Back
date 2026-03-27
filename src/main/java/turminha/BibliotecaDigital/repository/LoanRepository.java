package turminha.BibliotecaDigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import turminha.BibliotecaDigital.enums.LoanStatus;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.model.Loan;
import turminha.BibliotecaDigital.model.User;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    //Recurso do próprio Spring Data JPA -> Conta por USER e STATUS
    //X usuário tem Y empréstimos desse status
    //Ex: AndreSigma tem 2 ACTIVE -> retorna 2
    long countByUserAndStatus(User user, LoanStatus status);

    // Metodo para descobrirmos os livros que estão bombando
    long countByBook(Book book);

    //Metodo para descobrirmos os usuarios mais serelepes
    long countByUser(User user);
}
