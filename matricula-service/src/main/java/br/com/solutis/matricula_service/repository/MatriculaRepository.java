package br.com.solutis.matricula_service.repository;

import br.com.solutis.matricula_service.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByAlunoIdAndCursoId(Long alunoId, Long cursoId);
}
