package br.com.solutis.certificado_service.mapper;

import br.com.solutis.certificado_service.dto.CertificadoRequestDto;
import br.com.solutis.certificado_service.dto.CertificadoResponseDto;
import br.com.solutis.certificado_service.dto.CertificadoUpdateDto;
import br.com.solutis.certificado_service.dto.cursodto.CursoResponseDto;
import br.com.solutis.certificado_service.dto.usuariodto.UsuarioResponseDto;
import br.com.solutis.certificado_service.entity.Certificado;
import br.com.solutis.certificado_service.repository.CertificadoRepository;

public class CertificadoMapper {

    public static Certificado toEntity(CertificadoRequestDto requestDto){
        if (requestDto == null){
            return null;
        }

        Certificado certificado = new Certificado();
        certificado.setDataEmissao(requestDto.getDataEmissao());
        certificado.setArquivoUrl(requestDto.getArquivoUrl());
        /*
        certificado.setCursoId(requestDto.getCursoId());
        certificado.setUsuarioId(requestDto.getUsuarioId());
         */

        return certificado;
    }

    public static CertificadoResponseDto toResponseDto(Certificado certificado, CursoResponseDto curso,
                                                       UsuarioResponseDto usuario) {
        if (certificado == null){
            return null;
        }

        CertificadoResponseDto responseDto = new CertificadoResponseDto();
        responseDto.setId(certificado.getId());
        responseDto.setDataEmissao(certificado.getDataEmissao());
        responseDto.setArquivoUrl(certificado.getArquivoUrl());
        responseDto.setCurso(curso);
        responseDto.setUsuario(usuario);

        return responseDto;
    }

    public static Certificado toUpdateDto(CertificadoUpdateDto updateDto, Long idCertificado){
        if (updateDto == null) {
            return null;
        }

        Certificado certificado = new Certificado();
        certificado.setId(idCertificado);
        certificado.setDataEmissao(updateDto.getDataEmissao());

        return certificado;
    }
}
