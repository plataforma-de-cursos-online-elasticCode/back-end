package br.com.solutis.pagamento_service.service;

import br.com.solutis.pagamento_service.client.CursoClient;
import br.com.solutis.pagamento_service.client.UsuarioClient;
import br.com.solutis.pagamento_service.client.UsuarioResponseDto;
import br.com.solutis.pagamento_service.dto.PagamentoRequestDto;
import br.com.solutis.pagamento_service.dto.PagamentoResponseDto;
import br.com.solutis.pagamento_service.entity.Pagamento;
import br.com.solutis.pagamento_service.exception.EntidadeNaoEncontradaException;
import br.com.solutis.pagamento_service.mapper.PagamentoMapper;
import br.com.solutis.pagamento_service.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository repository;
    private final PagamentoMapper mapper = new PagamentoMapper();

    @Autowired
    private UsuarioClient usuarioClient;
    @Autowired
    private CursoClient cursoClient;

    // CREATE
    public PagamentoResponseDto cadastrar(PagamentoRequestDto dto, String token){
        usuarioClient
                .buscarUsuarioPorId(dto.getFkUsuario(), token)
                .blockOptional()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário com fk %d não encontrado!".formatted(dto.getFkUsuario())));

        cursoClient
                .buscarCursoPorId(dto.getFkCurso(), token)
                .blockOptional()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Curso com fk %d não encontrado!".formatted(dto.getFkCurso())));


        Pagamento pagamento = mapper.toEntity(dto);
        return mapper.toDto(repository.save(pagamento));
    }

    // READ
    public List<PagamentoResponseDto> listar(){
        return mapper.toListDto(repository.findAll());
    }

    public PagamentoResponseDto listarPorId(Long id){
        Pagamento pagamento = repository.findById(id.intValue())
                .orElseThrow(()-> new EntidadeNaoEncontradaException("Usuário com id %d não encontrado".formatted(id)));

        return mapper.toDto(pagamento);
    }

    // UPDATE
    public Pagamento atualizar(Long id, PagamentoRequestDto dto){
        Pagamento pagamento = repository.findById(id.intValue())
                .orElseThrow(()-> new EntidadeNaoEncontradaException("Usuário com id %d não encontrado".formatted(id)));

        pagamento.setIdPagamento(id);
        pagamento.setValor(dto.getValor());
        pagamento.setStatus(dto.getStatus());
        pagamento.setData(dto.getData());

        return repository.save(pagamento);
    }

    // DELETE
    public void deletar(Long id){
        if (repository.existsById(id.intValue())){
            throw new EntidadeNaoEncontradaException("Usuário com id %d não encontrado".formatted(id));
        }

        repository.deleteById(id.intValue());
    }
}
