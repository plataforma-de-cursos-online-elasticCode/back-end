package br.com.solutis.certificado_service.controller;

import br.com.solutis.certificado_service.dto.CertificadoRequestDto;
import br.com.solutis.certificado_service.dto.CertificadoResponseDto;
import br.com.solutis.certificado_service.service.CertificadoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/certificados")
public class CertificadoController {

    private final CertificadoService certificadoService;

    @PostMapping
    public ResponseEntity<CertificadoResponseDto> emitirCertificado(
            @RequestBody CertificadoRequestDto dto,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        CertificadoResponseDto response = certificadoService.emitirCertificado(dto, token);
        return ResponseEntity.ok(response);
    }

}
