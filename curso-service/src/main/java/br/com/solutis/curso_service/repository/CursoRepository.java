package br.com.solutis.curso_service.repository;

import br.com.solutis.curso_service.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByNome(String nome);

}
