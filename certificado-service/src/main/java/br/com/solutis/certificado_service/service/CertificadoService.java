package br.com.solutis.certificado_service.service;

import br.com.solutis.certificado_service.client.CursoClient;
import br.com.solutis.certificado_service.client.PdfClient;
import br.com.solutis.certificado_service.client.UsuarioClient;
import br.com.solutis.certificado_service.dto.CertificadoRequestDto;
import br.com.solutis.certificado_service.dto.CertificadoResponseDto;
import br.com.solutis.certificado_service.dto.cursodto.CursoResponseDto;
import br.com.solutis.certificado_service.dto.usuariodto.UsuarioResponseDto;
import br.com.solutis.certificado_service.entity.Certificado;
import br.com.solutis.certificado_service.mapper.CertificadoMapper;
import br.com.solutis.certificado_service.repository.CertificadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificadoService {

    private final CertificadoRepository repository;
    private final UsuarioClient usuarioClient;
    private final CursoClient cursoClient;
    private final PdfClient pdfClient;

    public CertificadoResponseDto emitirCertificado(CertificadoRequestDto requestDto, String token) {
        Certificado certificado = CertificadoMapper.toEntity(requestDto);
        certificado.setCursoId(requestDto.getCursoId());
        certificado.setUsuarioId(requestDto.getUsuarioId());

        CursoResponseDto curso = cursoClient.buscarCursoPorId(certificado.getCursoId());

        UsuarioResponseDto usuario = usuarioClient.buscarUsuarioPorId(certificado.getUsuarioId(), token);

        String pdfUrl = pdfClient.gerarPdf(curso.getNome(), usuario.getNome(), certificado.getDataEmissao());

        certificado.setArquivoUrl(pdfUrl);
        certificado = repository.save(certificado);

        return CertificadoMapper.toResponseDto(certificado, curso, usuario);
    }



}
