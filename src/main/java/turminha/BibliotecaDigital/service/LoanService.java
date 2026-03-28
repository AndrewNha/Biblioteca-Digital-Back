package turminha.BibliotecaDigital.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.enums.LoanStatus;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.model.Loan;
import turminha.BibliotecaDigital.model.User;
import turminha.BibliotecaDigital.repository.BookRepository;
import turminha.BibliotecaDigital.repository.LoanRepository;
import turminha.BibliotecaDigital.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReservationService reservationService;


    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository, ReservationService reservationService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reservationService = reservationService;
    }

    // Os services são responsáveis pelas regras de negócio

    //LISTAR TODOS
    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    //LISTAR UM
    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found."));
    }

    //CRIAR
    @Transactional
    public Loan save(Loan loan) {
        Book book = bookRepository.findById(loan.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findById(loan.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        //Esses throw é importante pq um null o controller n ia entender nada
        //O controller só entende HTTP, nesse caso das exceções "retorna"
        //Um bad request, ou seja, deu ruim fi
        if (book.getQuantityAvailable() == null || book.getQuantityAvailable() <= 0) {
            throw new RuntimeException("No copies available for this book.");
        }

        long activeLoans = loanRepository.countByUserAndStatus(user, LoanStatus.ACTIVE) +
                           loanRepository.countByUserAndStatus(user, LoanStatus.LATE);

        if (activeLoans >= 3) {
            throw new RuntimeException("User already has 3 active loans.");
        }

        //Mais empréstimo -> Menos livro disponivel
        book.setQuantityAvailable(book.getQuantityAvailable() - 1);

        //Evitar livro e usuário fantasma
        loan.setBook(book);
        loan.setUser(user);

        loan.setLoanDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE);

        return loanRepository.save(loan);
    }

    //RETORNAR EMPRÉSTIMO
    @Transactional
    public Loan returnBook(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found."));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("This loan is already closed.");
        }

        loan.setStatus(LoanStatus.RETURNED);

        boolean hadReservation = reservationService.completeNextReservation(loan.getBook());

        if (!hadReservation) {
            loan.getBook().setQuantityAvailable(loan.getBook().getQuantityAvailable() + 1);
        }

        return loanRepository.save(loan);
    }


    //DELETAR
    @Transactional
    public void delete(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found."));

        if (loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.LATE) {
            //MAIS BUG HAHAHAHAHAHAHHAHAHAHHAHAHAHAHAHHAHAHAHHAHAHA
            boolean hadReservation = reservationService.completeNextReservation(loan.getBook());

            if (!hadReservation) {
                loan.getBook().setQuantityAvailable(loan.getBook().getQuantityAvailable() + 1);
            }

        }

        loanRepository.delete(loan);
    }

    //UPDATE

    @Transactional
    public Loan update(Long id, Loan loanUpdated) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found."));

        if (bookChanged(loan, loanUpdated) && wasActive(loan)) {
            Book book = bookRepository.findById(loanUpdated.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Book not found."));

            if (book.getQuantityAvailable() <= 0) {
                throw new RuntimeException("No copies available for this book.");
            }

            returnCopy(loan.getBook());
            borrowCopy(book);
            loanUpdated.setBook(book);

        } else if (bookChanged(loan, loanUpdated) && !wasActive(loan)){
                throw new RuntimeException("Cannot change the book of a returned loan.");
        } else {
            loanUpdated.setBook(loan.getBook());
        }

        if (userChanged(loan, loanUpdated)) {
            User user = userRepository.findById(loanUpdated.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found."));

            long activeLoans = loanRepository.countByUserAndStatus(user, LoanStatus.ACTIVE)
                    + loanRepository.countByUserAndStatus(user, LoanStatus.LATE);

            if (activeLoans >= 3) {
                throw new RuntimeException("User already has 3 active loans.");
            }

            loanUpdated.setUser(user);

        } else {
            loanUpdated.setUser(loan.getUser());
        }



        loan.setUser(loanUpdated.getUser());
        loan.setBook(loanUpdated.getBook());
        loan.setLoanDate(loanUpdated.getLoanDate());
        loan.setReturnDate(loanUpdated.getReturnDate());
        loan.setStatus(loanUpdated.getStatus());

        return loanRepository.save(loan);
    }

    // Livro mais bombástico
    public Book mostBorrowedBook() {
        Book mostBorrowed = null;
        long max = 0;

        for (Book book : bookRepository.findAll()) {
            long count = loanRepository.countByBook(book);
            if (count > max) {
                max = count;
                mostBorrowed = book;
            }
        }

        return mostBorrowed;
    }

    // Usuario mais bombástico
    public User mostActiveUser() {
        User mostActive = null;
        long max = 0;

        for (User user : userRepository.findAll()) {
            long count = loanRepository.countByUser(user);
            if (count > max) {
                max = count;
                mostActive = user;
            }
        }

        return mostActive;
    }

    private boolean bookChanged(Loan loan, Loan loanUpdated) {
        return !loan.getBook().getId().equals(loanUpdated.getBook().getId());
        //Se o livro permanece o mesmo, retorna falso
    }

    private boolean wasActive(Loan loan) {
        return loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.LATE;
        //Se o empréstimo estava ativo ou atrasado retorna true
    }

    private void returnCopy(Book book) {
        book.setQuantityAvailable(book.getQuantityAvailable() + 1);
        //Devolve a copia
    }

    private void borrowCopy(Book book) {
        book.setQuantityAvailable(book.getQuantityAvailable() - 1);
        //Diminui a quantidade disponivel do livro atualizado (novo)
    }

    private boolean userChanged(Loan loan, Loan loanUpdated) {
        return !loan.getUser().getId().equals(loanUpdated.getUser().getId());
    }
}
