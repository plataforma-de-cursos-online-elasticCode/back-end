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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificadoServiceTest {

    @InjectMocks
    private CertificadoService certificadoService;

    @Mock
    private CertificadoRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private CursoClient cursoClient;

    @Mock
    private PdfClient pdfClient;


    @Test
    @DisplayName("Deve emitir um certificado com sucesso")
    void deveEmitirCertificadoComSucesso() {

        // Arrange
        Long cursoId = 1L;
        Long usuarioId = 2L;
        String token = "Bearer token";
        String urlMock = "http://pdf.com/certificado.pdf";

        CertificadoRequestDto requestDto = new CertificadoRequestDto();
        requestDto.setCursoId(cursoId);
        requestDto.setUsuarioId(usuarioId);

        CursoResponseDto cursoDto = new CursoResponseDto();
        cursoDto.setNome("Curso de Testes");

        UsuarioResponseDto usuarioDto = new UsuarioResponseDto();
        usuarioDto.setId(usuarioId);
        usuarioDto.setNome("João da Silva");

        Certificado certificado = new Certificado();
        certificado.setCursoId(cursoId);
        certificado.setUsuarioId(usuarioId);
        certificado.setDataEmissao(LocalDate.now());

        Certificado certificadoSalvo = new Certificado();
        certificadoSalvo.setCursoId(cursoId);
        certificadoSalvo.setUsuarioId(usuarioId);
        certificadoSalvo.setDataEmissao(certificado.getDataEmissao());
        certificadoSalvo.setArquivoUrl(urlMock);

        CertificadoResponseDto responseEsperado = new CertificadoResponseDto();
        responseEsperado.setCurso(cursoDto);
        responseEsperado.setUsuario(usuarioDto);
        responseEsperado.setArquivoUrl(urlMock);
        responseEsperado.setDataEmissao(certificado.getDataEmissao());

        try (MockedStatic<CertificadoMapper> mapperMock = mockStatic(CertificadoMapper.class)) {
            mapperMock.when(() -> CertificadoMapper.toEntity(requestDto)).thenReturn(certificado);
            mapperMock.when(() -> CertificadoMapper.toResponseDto(certificadoSalvo, cursoDto, usuarioDto))
                    .thenReturn(responseEsperado);

            when(cursoClient.buscarCursoPorId(cursoId)).thenReturn(cursoDto);
            when(usuarioClient.buscarUsuarioPorId(usuarioId, token)).thenReturn(usuarioDto);
            when(pdfClient.gerarPdf(eq("Curso de Testes"), eq("João da Silva"), any()))
                    .thenReturn(urlMock);
            when(repository.save(certificado)).thenReturn(certificadoSalvo);

            // Act
            CertificadoResponseDto response = certificadoService.emitirCertificado(requestDto, token);

            // Assert
            assertNotNull(response);
            assertEquals("Curso de Testes", response.getCurso().getNome());
            assertEquals("João da Silva", response.getUsuario().getNome());
            assertEquals(urlMock, response.getArquivoUrl());

            verify(cursoClient).buscarCursoPorId(cursoId);
            verify(usuarioClient).buscarUsuarioPorId(usuarioId, token);
            verify(pdfClient).gerarPdf(eq("Curso de Testes"), eq("João da Silva"), any());
            verify(repository).save(certificado);
            mapperMock.verify(() -> CertificadoMapper.toEntity(requestDto));
            mapperMock.verify(() -> CertificadoMapper.toResponseDto(certificadoSalvo, cursoDto, usuarioDto));
        }
    }
}