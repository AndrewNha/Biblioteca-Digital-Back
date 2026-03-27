package turminha.BibliotecaDigital.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.enums.ReservationStatus;
import turminha.BibliotecaDigital.model.Book;
import turminha.BibliotecaDigital.model.Reservation;
import turminha.BibliotecaDigital.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    //LISTAR TODOS
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    //CRIAR - só permite reservar se não houver cópias disponíveis
    @Transactional
    public Reservation save(Reservation reservation) {
        Book book = reservation.getBook();

        if (book.getQuantityAvailable() > 0) {
            throw new RuntimeException("Book has copies available. Make a loan instead.");
        }

        // Usuário não pode reservar o mesmo livro duas vezes
        if (reservationRepository.existsByUserAndBookAndStatus(
                reservation.getUser(), reservation.getBook(), ReservationStatus.PENDING)) {
            throw new RuntimeException("User already has a pending reservation for this book.");
        }

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