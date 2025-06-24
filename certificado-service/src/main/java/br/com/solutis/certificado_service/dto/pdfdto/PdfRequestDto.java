package br.com.solutis.certificado_service.dto.pdfdto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PdfRequestDto {
    private String nomeCurso;
    private String nomeUsuario;
    private LocalDate dataEmissao;
}
