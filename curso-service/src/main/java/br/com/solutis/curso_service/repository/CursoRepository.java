package br.com.solutis.curso_service.repository;

import br.com.solutis.curso_service.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findByNome(String nome);

}
