package br.com.solutis.curso_service.service;

import br.com.solutis.curso_service.dto.CursoRequestDto;
import br.com.solutis.curso_service.dto.CursoResponseDto;
import br.com.solutis.curso_service.entity.Curso;
import br.com.solutis.curso_service.exception.CursoNaoEncontradoException;
import br.com.solutis.curso_service.mapper.CursoMapper;
import br.com.solutis.curso_service.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceTest {

    @Mock
    private CursoRepository repository;

    @InjectMocks
    private CursoService service;

    private AutoCloseable closeable;

    private Curso curso;
    private CursoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Java");
        curso.setDescricao("Curso de Java");
        curso.setPreco(100.0);
        curso.setCategoria("Programação");

        requestDto = new CursoRequestDto("Java", "Curso de Java", 100.0, "Programação");
    }

    @Test
    void cadastrarCurso_deveRetornarCursoResponseDto() {
        when(repository.save(any(Curso.class))).thenReturn(curso);

        CursoResponseDto response = service.cadastrarCurso(requestDto);

        assertEquals("Java", response.nome());
        verify(repository).save(any(Curso.class));
    }

    @Test
    void listarTodosCursos_deveRetornarListaDeCursoResponseDto() {
        when(repository.findAll()).thenReturn(List.of(curso));

        List<CursoResponseDto> lista = service.listarTodosCursos();

        assertEquals(1, lista.size());
        assertEquals("Java", lista.get(0).nome());
    }

    @Test
    void buscarCursoPorId_quandoExiste_deveRetornarCursoResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso));

        CursoResponseDto response = service.buscarCursoPorId(1L);

        assertEquals("Java", response.nome());
    }

    @Test
    void buscarCursoPorId_quandoNaoExiste_deveLancarExcecao() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CursoNaoEncontradoException.class, () -> service.buscarCursoPorId(1L));
    }

    @Test
    void buscarCursoPorNome_quandoExiste_deveRetornarCursoResponseDto() {
        when(repository.findByNome("Java")).thenReturn(Optional.of(curso));

        CursoResponseDto response = service.buscarCursoPorNome("Java");

        assertEquals("Java", response.nome());
    }

    @Test
    void buscarCursoPorNome_quandoNaoExiste_deveLancarExcecao() {
        when(repository.findByNome("Python")).thenReturn(Optional.empty());

        assertThrows(CursoNaoEncontradoException.class, () -> service.buscarCursoPorNome("Python"));
    }

    @Test
    void deletarCursoPorId_quandoExiste_deveDeletar() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso));

        service.deletarCursoPorId(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deletarCursoPorId_quandoNaoExiste_deveLancarExcecao() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CursoNaoEncontradoException.class, () -> service.deletarCursoPorId(1L));
    }

    @Test
    void atualizarCursoPorId_quandoExiste_deveAtualizarECadastrar() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso));
        when(repository.save(any(Curso.class))).thenReturn(curso);

        Curso atualizado = service.atualizarCursoPorId(1L, requestDto);

        assertEquals("Java", atualizado.getNome());
        verify(repository).save(any(Curso.class));
    }

    @Test
    void atualizarCursoPorId_quandoNaoExiste_deveLancarExcecao() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CursoNaoEncontradoException.class, () -> service.atualizarCursoPorId(1L, requestDto));
    }

    @BeforeEach
    void tearDown() throws Exception {
        if (closeable != null) closeable.close();
    }
}
