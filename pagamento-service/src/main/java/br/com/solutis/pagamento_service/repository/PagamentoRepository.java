package br.com.solutis.pagamento_service.repository;

import br.com.solutis.pagamento_service.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}
