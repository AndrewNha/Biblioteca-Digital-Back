package turminha.BibliotecaDigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import turminha.BibliotecaDigital.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
