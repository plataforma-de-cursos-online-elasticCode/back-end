package br.com.solutis.pagamento_service.mapper;

import br.com.solutis.pagamento_service.dto.PagamentoRequestDto;
import br.com.solutis.pagamento_service.dto.PagamentoResponseDto;
import br.com.solutis.pagamento_service.entity.Pagamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagamentoMapper {
    public Pagamento toEntity(PagamentoRequestDto dto){
        Pagamento pagamento = new Pagamento();

        pagamento.setValor(dto.getValor());
        pagamento.setStatus(dto.getStatus());
        pagamento.setData(new Date());
        pagamento.setFkCurso(dto.getFkCurso());
        pagamento.setFkUsuario(dto.getFkUsuario());

        return pagamento;
    }

    public PagamentoResponseDto toDto(Pagamento entity){
        PagamentoResponseDto dto = new PagamentoResponseDto();

        dto.setIdPagamento(entity.getIdPagamento());
        dto.setValor(entity.getValor());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    public List<PagamentoResponseDto> toListDto(List<Pagamento> entitys){
        List<PagamentoResponseDto> dtos = new ArrayList<>();

        for(Pagamento pag : entitys){
            dtos.add(toDto(pag));
        }

        return dtos;
    }
}
