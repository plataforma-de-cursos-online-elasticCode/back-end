package br.com.solutis.pagamento_service.dto;

import br.com.solutis.pagamento_service.entity.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PagamentoRequestDto {
    private Double valor;
    private Status status;
    private Date data;
    private Long fkCurso;
    private Long fkUsuario;

    public PagamentoRequestDto(long l, long l1, double v, String pago, LocalDate now) {
    }

    public PagamentoRequestDto() {

    }
}
