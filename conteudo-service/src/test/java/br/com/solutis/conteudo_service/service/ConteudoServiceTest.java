package br.com.solutis.conteudo_service.service;

import br.com.solutis.conteudo_service.client.CursoClient;
import br.com.solutis.conteudo_service.dto.ConteudoRequestDto;
import br.com.solutis.conteudo_service.entity.Conteudo;
import br.com.solutis.conteudo_service.exception.ConteudoNaoEncontradoException;
import br.com.solutis.conteudo_service.repository.ConteudoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConteudoServiceTest {

    private ConteudoRepository repository;
    private CursoClient cursoClient;
    private ConteudoService service;

    @BeforeEach
    void setup() {
        repository = mock(ConteudoRepository.class);
        cursoClient = mock(CursoClient.class);
        service = new ConteudoService(cursoClient);
        try {
            var field = ConteudoService.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void cadastrarConteudo_deveChamarCursoClientEBanco() {
        ConteudoRequestDto request = new ConteudoRequestDto("nome", "url", 1L);

        when(cursoClient.buscarCursoPorId(1L)).thenReturn(null); // só para não lançar exceção
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Conteudo result = service.cadastrarConteudo(request);

        verify(cursoClient).buscarCursoPorId(1L);

        ArgumentCaptor<Conteudo> captor = ArgumentCaptor.forClass(Conteudo.class);
        verify(repository).save(captor.capture());

        Conteudo saved = captor.getValue();
        assertEquals("nome", saved.getNome());
        assertEquals("url", saved.getVideoUrl());
        assertEquals(1L, saved.getFkCurso());

        assertEquals(saved, result);
    }

    @Test
    void listarTodosConteudos_deveRetornarTodos() {
        Conteudo c1 = new Conteudo();
        c1.setId(1L);
        Conteudo c2 = new Conteudo();
        c2.setId(2L);

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<Conteudo> result = service.listarTodosConteudos();

        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }

    @Test
    void buscarConteudoPorId_quandoExiste_deveRetornar() {
        Conteudo c = new Conteudo();
        c.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(c));

        Conteudo result = service.buscarConteudoPorId(1L);

        assertEquals(c, result);
    }

    @Test
    void buscarConteudoPorId_quandoNaoExiste_deveLancarExcecao() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ConteudoNaoEncontradoException.class, () -> service.buscarConteudoPorId(1L));
        assertTrue(ex.getMessage().contains("Conteudo de id 1 não encontrado"));
    }

    @Test
    void deletarConteudoPorId_quandoExiste_deveDeletar() {
        Conteudo c = new Conteudo();
        c.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(c));

        service.deletarConteudoPorId(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deletarConteudoPorId_quandoNaoExiste_deveLancarExcecao() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ConteudoNaoEncontradoException.class, () -> service.deletarConteudoPorId(1L));
        assertTrue(ex.getMessage().contains("Conteudo de id 1 não encontrado"));
    }

    @Test
    void atualizarConteudoPorId_quandoExiste_deveAtualizarESalvar() {
        Conteudo c = new Conteudo();
        c.setId(1L);
        c.setNome("velho");
        c.setVideoUrl("urlVelho");
        c.setFkCurso(1L);

        ConteudoRequestDto request = new ConteudoRequestDto("novo", "urlNovo", 2L);

        when(repository.findById(1L)).thenReturn(Optional.of(c));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Conteudo updated = service.atualizarConteudoPorId(1L, request);

        assertEquals("novo", updated.getNome());
        assertEquals("urlNovo", updated.getVideoUrl());
        assertEquals(2L, updated.getFkCurso());
    }

    @Test
    void atualizarConteudoPorId_quandoNaoExiste_deveLancarExcecao() {
        ConteudoRequestDto request = new ConteudoRequestDto("novo", "urlNovo", 2L);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ConteudoNaoEncontradoException.class,
                () -> service.atualizarConteudoPorId(1L, request));
        assertTrue(ex.getMessage().contains("Conteudo não encontrado"));
    }
}
