package turminha.BibliotecaDigital.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import turminha.BibliotecaDigital.model.Author;
import turminha.BibliotecaDigital.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    //LISTAR TODOS
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    //CRIAR
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    //DELETAR
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found."));
        authorRepository.delete(author);
    }

    //UPDATE
    @Transactional
    public Author update(Long id, Author authorUpdated) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found."));

        author.setName(authorUpdated.getName());
        author.setNationality(authorUpdated.getNationality());

        return authorRepository.save(author);
    }
}
