package br.com.solutis.certificado_service.dto;

import br.com.solutis.certificado_service.dto.cursodto.CursoResponseDto;
import br.com.solutis.certificado_service.dto.usuariodto.UsuarioResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificadoResponseDto {

    private Long id;
    private LocalDate dataEmissao;
    private String arquivoUrl;
    private CursoResponseDto curso;
    private UsuarioResponseDto usuario;
}


