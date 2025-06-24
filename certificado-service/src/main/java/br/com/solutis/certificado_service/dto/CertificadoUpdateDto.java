package br.com.solutis.certificado_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificadoUpdateDto {

    @NotNull
    @PastOrPresent
    private LocalDate dataEmissao;
}
