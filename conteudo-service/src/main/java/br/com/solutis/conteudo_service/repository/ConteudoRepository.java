package br.com.solutis.conteudo_service.repository;

import br.com.solutis.conteudo_service.entity.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {
}
