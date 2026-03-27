package turminha.BibliotecaDigital.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.model.Reservation;
import turminha.BibliotecaDigital.model.User;
import turminha.BibliotecaDigital.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
