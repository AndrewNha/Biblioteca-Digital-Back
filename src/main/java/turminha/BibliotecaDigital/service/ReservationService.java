package turminha.BibliotecaDigital.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.enums.ReservationStatus;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.model.Loan;
import turminha.BibliotecaDigital.model.Reservation;
import turminha.BibliotecaDigital.model.User;
import turminha.BibliotecaDigital.repository.BookRepository;
import turminha.BibliotecaDigital.repository.ReservationRepository;
import turminha.BibliotecaDigital.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    //LISTAR TODOS
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    //LISTAR UM
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found."));
    }

    //CRIAR - só permite reservar se não houver cópias disponíveis
    @Transactional
    public Reservation save(Reservation reservation) {
        Book book = bookRepository.findById(reservation.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found."));

        User user = userRepository.findById(reservation.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (book.getQuantityAvailable() == null || book.getQuantityAvailable() > 0) {
            throw new RuntimeException("Book has copies available. Make a loan instead.");
        }

        // Usuário não pode reservar o mesmo livro duas vezes
        if (reservationRepository.existsByUserAndBookAndStatus(
                user, book, ReservationStatus.PENDING)) {
            throw new RuntimeException("User already has a pending reservation for this book.");
        }

        //Evitar livro e usuário fantasma, igual fiz no Loan
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationRepository.save(reservation);
    }

    //CANCELAR
    @Transactional
    public Reservation cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found."));

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new RuntimeException("Reservation already completed.");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Reservation already cancelled.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    //COMPLETAR — chamado quando o livro é devolvido e há reserva pendente
    @Transactional
    public boolean completeNextReservation(Book book) {
        Reservation next = reservationRepository
                .findFirstByBookAndStatusOrderByReservationDateAsc(book, ReservationStatus.PENDING);

        if (next != null) {
            next.setStatus(ReservationStatus.COMPLETED);
            reservationRepository.save(next);
            // decrementa disponibilidade pois já vai pro próximo da fila
            return true;
        }

        return false;
    }

    //DELETAR
    @Transactional
    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found."));
        reservationRepository.delete(reservation);
    }

    //UPDATE
    @Transactional
    public Reservation update(Long id, Reservation reservationUpdated) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found."));

        reservation.setUser(reservationUpdated.getUser());
        reservation.setBook(reservationUpdated.getBook());
        reservation.setReservationDate(reservationUpdated.getReservationDate());
        reservation.setStatus(reservationUpdated.getStatus());

        return reservationRepository.save(reservation);
    }
}