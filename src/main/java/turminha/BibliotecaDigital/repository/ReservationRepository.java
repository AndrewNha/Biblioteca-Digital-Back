package turminha.BibliotecaDigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import turminha.BibliotecaDigital.enums.ReservationStatus;
import turminha.BibliotecaDigital.model.Reservation;
import turminha.BibliotecaDigital.model.User;
import turminha.BibliotecaDigital.model.Book;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //Isso é para impedir que um usuário seja deletado com uma reserva pendente (outro bug cara q ÓDIO meu irmao)
    boolean existsByUserAndStatus(User user, ReservationStatus status);

    //Verficar para impedir de um usuário reservar o mesmo livro 2x
    boolean existsByUserAndBookAndStatus(User user, Book book, ReservationStatus status);

    //Verifica qual a reserva pendente MAIS ANTIGA para ser a prox na fila
    Reservation findFirstByBookAndStatusOrderByReservationDateAsc(Book book, ReservationStatus status);

}