package br.com.solutis.conteudo_service.controller;

import br.com.solutis.conteudo_service.dto.ConteudoRequestDto;
import br.com.solutis.conteudo_service.entity.Conteudo;
import br.com.solutis.conteudo_service.service.ConteudoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConteudoControllerTest {

    private MockMvc mockMvc;
    private ConteudoService service;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        service = mock(ConteudoService.class);
        ConteudoController controller = new ConteudoController();
        try {
            var field = ConteudoController.class.getDeclaredField("service");
            field.setAccessible(true);
            field.set(controller, service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void cadastrarConteudo_deveRetornar201ComConteudo() throws Exception {
        ConteudoRequestDto request = new ConteudoRequestDto("nome", "url", 1L);
        Conteudo resposta = new Conteudo();
        resposta.setId(10L);
        resposta.setNome("nome");
        resposta.setVideoUrl("url");
        resposta.setFkCurso(1L);

        when(service.cadastrarConteudo(any())).thenReturn(resposta);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("nome"))
                .andExpect(jsonPath("$.videoUrl").value("url"))
                .andExpect(jsonPath("$.fkCurso").value(1));

        verify(service).cadastrarConteudo(any());
    }

    @Test
    void listarTodosConteudos_deveRetornar200ComLista() throws Exception {
        Conteudo c1 = new Conteudo();
        c1.setId(1L);
        Conteudo c2 = new Conteudo();
        c2.setId(2L);

        when(service.listarTodosConteudos()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(service).listarTodosConteudos();
    }

    @Test
    void buscarConteudoPorId_deveRetornarConteudo() throws Exception {
        Conteudo c = new Conteudo();
        c.setId(5L);
        c.setNome("teste");

        when(service.buscarConteudoPorId(5L)).thenReturn(c);

        mockMvc.perform(get("/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.nome").value("teste"));

        verify(service).buscarConteudoPorId(5L);
    }

    @Test
    void deletarConteudoPorId_deveRetornar204() throws Exception {
        doNothing().when(service).deletarConteudoPorId(3L);

        mockMvc.perform(delete("/3"))
                .andExpect(status().isNoContent());

        verify(service).deletarConteudoPorId(3L);
    }

    @Test
    void atualizarConteudo_deveRetornarConteudoAtualizado() throws Exception {
        ConteudoRequestDto request = new ConteudoRequestDto("nomeAtualizado", "urlAtualizado", 4L);
        Conteudo cAtualizado = new Conteudo();
        cAtualizado.setId(7L);
        cAtualizado.setNome("nomeAtualizado");
        cAtualizado.setVideoUrl("urlAtualizado");
        cAtualizado.setFkCurso(4L);

        when(service.atualizarConteudoPorId(eq(7L), any())).thenReturn(cAtualizado);

        mockMvc.perform(put("/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.nome").value("nomeAtualizado"))
                .andExpect(jsonPath("$.videoUrl").value("urlAtualizado"))
                .andExpect(jsonPath("$.fkCurso").value(4));

        verify(service).atualizarConteudoPorId(eq(7L), any());
    }
}
