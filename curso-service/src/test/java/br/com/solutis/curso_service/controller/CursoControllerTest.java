package br.com.solutis.curso_service.controller;

import br.com.solutis.curso_service.dto.CursoRequestDto;
import br.com.solutis.curso_service.dto.CursoResponseDto;
import br.com.solutis.curso_service.entity.Curso;
import br.com.solutis.curso_service.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CursoService service;

    @Autowired
    private ObjectMapper objectMapper;

    CursoResponseDto mockResponseDto() {
        return new CursoResponseDto(1L, "Java", "Curso de Java", 120.0, "Programação");
    }

    CursoRequestDto mockRequestDto() {
        return new CursoRequestDto("Java", "Curso de Java", 120.0, "Programação");
    }

    @Test
    void cadastrarCurso_deveRetornar200() throws Exception {
        Mockito.when(service.cadastrarCurso(any())).thenReturn(mockResponseDto());

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Java")));
    }

    @Test
    void cadastrarCurso_deveRetornar409_EmCasoDeViolacao() throws Exception {
        Mockito.when(service.cadastrarCurso(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequestDto())))
                .andExpect(status().isConflict());
    }

    @Test
    void listarTodosCursos_deveRetornarLista() throws Exception {
        Mockito.when(service.listarTodosCursos()).thenReturn(List.of(mockResponseDto()));

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Java")));
    }

    @Test
    void buscarCursoPorId_deveRetornarCurso() throws Exception {
        Mockito.when(service.buscarCursoPorId(1L)).thenReturn(mockResponseDto());

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Java")));
    }

    @Test
    void buscarCursoPorNome_deveRetornarCurso() throws Exception {
        Mockito.when(service.buscarCursoPorNome("Java")).thenReturn(mockResponseDto());

        mockMvc.perform(get("/buscarPorNome")
                        .param("nome", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Java")));
    }

    @Test
    void deletarCursoPorId_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/deletar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void atualizarCursoPorId_deveRetornarCursoAtualizado() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Java Atualizado");
        curso.setDescricao("Atualizado");
        curso.setPreco(130.0);
        curso.setCategoria("Programação");

        Mockito.when(service.atualizarCursoPorId(Mockito.eq(1L), any())).thenReturn(curso);

        mockMvc.perform(put("/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Java Atualizado")));
    }
}
