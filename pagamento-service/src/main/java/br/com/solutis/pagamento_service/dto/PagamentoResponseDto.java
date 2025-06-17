package br.com.solutis.pagamento_service.dto;

import br.com.solutis.pagamento_service.entity.Status;
import lombok.Data;

@Data
public class PagamentoResponseDto {
    private Long idPagamento;
    private Double valor;
    private Status status;
}
