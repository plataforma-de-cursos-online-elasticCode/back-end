package br.com.solutis.pagamento_service;

import br.com.solutis.pagamento_service.client.CursoClient;
import br.com.solutis.pagamento_service.client.UsuarioClient;
import br.com.solutis.pagamento_service.client.UsuarioResponseDto;
import br.com.solutis.pagamento_service.dto.PagamentoRequestDto;
import br.com.solutis.pagamento_service.dto.PagamentoResponseDto;
import br.com.solutis.pagamento_service.entity.Pagamento;
import br.com.solutis.pagamento_service.exception.EntidadeNaoEncontradaException;
import br.com.solutis.pagamento_service.mapper.PagamentoMapper;
import br.com.solutis.pagamento_service.repository.PagamentoRepository;
import br.com.solutis.pagamento_service.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private CursoClient cursoClient;

    private PagamentoMapper mapper = new PagamentoMapper();

    private PagamentoRequestDto requestDto;
    private Pagamento pagamento;
    private PagamentoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new PagamentoRequestDto(1L, 1L, 100.0, "PAGO", LocalDate.now());
        pagamento = mapper.toEntity(requestDto);
        pagamento.setIdPagamento(1L);
        responseDto = mapper.toDto(pagamento);

        WebClient.Builder builder = WebClient.builder();
        usuarioClient = new UsuarioClient(builder);
        cursoClient = new CursoClient(builder);

        ReflectionTestUtils.setField(service, "usuarioClient", usuarioClient);
        ReflectionTestUtils.setField(service, "cursoClient", cursoClient);
        ReflectionTestUtils.setField(service, "repository", repository);

    }

    // TESTE CADASTRAR
    @Test
    void deveCadastrarPagamentoComSucesso() {
        UsuarioResponseDto usuarioMock = new UsuarioResponseDto(1L, "Fulano", "fulano@email.com", null);
        Object cursoMock = new Object(); // ou CursoResponseDto

        doReturn(Mono.just(usuarioMock))
                .when(usuarioClient)
                .buscarUsuarioPorId(eq(1L), anyString());

        doReturn(Mono.just(cursoMock))
                .when(cursoClient)
                .buscarCursoPorId(eq(1L), anyString());

        when(repository.save(any(Pagamento.class)))
                .thenReturn(pagamento);

        PagamentoResponseDto result = service.cadastrar(requestDto, "token");

        assertEquals(responseDto.getValor(), result.getValor());
    }





    @Test
    void deveLancarExcecaoAoCadastrarPagamentoUsuarioInvalido() {
        when(usuarioClient.buscarUsuarioPorId(anyLong(), anyString()))
                .thenReturn(Mono.empty());

        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class, () ->
                service.cadastrar(requestDto, "token"));

        assertTrue(ex.getMessage().contains("Usuário com fk 1 não encontrado"));
    }

    // TESTE LISTAR
    @Test
    void deveListarPagamentosComSucesso() {
        when(repository.findAll()).thenReturn(List.of(pagamento));
        List<PagamentoResponseDto> result = service.listar();
        assertEquals(1, result.size());
    }

    @Test
    void deveRetornarListaVaziaAoListar() {
        when(repository.findAll()).thenReturn(List.of());
        List<PagamentoResponseDto> result = service.listar();
        assertTrue(result.isEmpty());
    }

    // TESTE LISTAR POR ID
    @Test
    void deveListarPagamentoPorIdComSucesso() {
        when(repository.findById(1)).thenReturn(Optional.of(pagamento));
        PagamentoResponseDto result = service.listarPorId(1L);
        assertEquals(pagamento.getValor(), result.getValor());
    }

    @Test
    void deveLancarExcecaoAoListarPagamentoPorIdInexistente() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () ->
                service.listarPorId(1L));
    }

    // TESTE ATUALIZAR
    @Test
    void deveAtualizarPagamentoComSucesso() {
        when(repository.findById(1)).thenReturn(Optional.of(pagamento));
        when(repository.save(any())).thenReturn(pagamento);

        Pagamento result = service.atualizar(1L, requestDto);
        assertEquals(requestDto.getValor(), result.getValor());
    }

    @Test
    void deveLancarExcecaoAoAtualizarPagamentoInexistente() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                service.atualizar(1L, requestDto));
    }

    // TESTE DELETAR
    @Test
    void deveDeletarPagamentoComSucesso() {
        when(repository.existsById(1)).thenReturn(false);

        assertDoesNotThrow(() -> service.deletar(1L));
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void deveLancarExcecaoAoDeletarPagamentoInexistente() {
        when(repository.existsById(1)).thenReturn(true);

        assertThrows(EntidadeNaoEncontradaException.class, () ->
                service.deletar(1L));
    }
}
