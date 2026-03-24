package turminha.BibliotecaDigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import turminha.BibliotecaDigital.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
