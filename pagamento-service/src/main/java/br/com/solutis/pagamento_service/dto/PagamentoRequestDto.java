package br.com.solutis.pagamento_service.dto;

import br.com.solutis.pagamento_service.entity.Status;
import lombok.Data;

import java.util.Date;

@Data
public class PagamentoRequestDto {
    private Double valor;
    private Status status;
    private Date data;
    private Long fkCurso;
    private Long fkUsuario;
}
