package turminha.BibliotecaDigital.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.enums.LoanStatus;
import turminha.BibliotecaDigital.enums.ReservationStatus;
import turminha.BibliotecaDigital.model.Reservation;
import turminha.BibliotecaDigital.model.User;
import turminha.BibliotecaDigital.repository.LoanRepository;
import turminha.BibliotecaDigital.repository.ReservationRepository;
import turminha.BibliotecaDigital.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public UserService(UserRepository userRepository, LoanRepository loanRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    //LISTAR TODOS
    public List<User> getAll() {
        return userRepository.findAll();
    }

    //LISTAR UM
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    //CRIAR
    public User save(User user) {
        return userRepository.save(user);
    }

    //DELETAR
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        long activeLoans = loanRepository.countByUserAndStatus(user, LoanStatus.ACTIVE)
                + loanRepository.countByUserAndStatus(user, LoanStatus.LATE);

        boolean hasPendingReservation = reservationRepository.existsByUserAndStatus(user, ReservationStatus.PENDING);

        //Correção de bug marota kkk AAAAAAAAAAAAAAAA
        if (activeLoans > 0) {
            throw new RuntimeException("Cannot delete user with active loans.");
        }

        //Correção de bug marota kkk AAAAAAAAAAAAAAAA
        if (hasPendingReservation) {
            throw new RuntimeException("Cannot delete user with pending reservations.");
        }

        userRepository.delete(user);
    }

    //UPDATE
    @Transactional
    public User update(Long id, User userUpdated) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setName(userUpdated.getName());
        user.setEmail(userUpdated.getEmail());
        user.setTelephoneNumber(userUpdated.getTelephoneNumber());

        return userRepository.save(user);
    }

}
