package turminha.BibliotecaDigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import turminha.BibliotecaDigital.enums.ReservationStatus;
import turminha.BibliotecaDigital.model.Reservation;
import turminha.BibliotecaDigital.model.User;
import turminha.BibliotecaDigital.model.Book;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByUserAndBookAndStatus(User user, Book book, ReservationStatus status);

    Reservation findFirstByBookAndStatusOrderByReservationDateAsc(Book book, ReservationStatus status);
}