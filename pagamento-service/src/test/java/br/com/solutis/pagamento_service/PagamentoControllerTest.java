package br.com.solutis.pagamento_service;

import br.com.solutis.pagamento_service.dto.PagamentoRequestDto;
import br.com.solutis.pagamento_service.entity.Pagamento;
import br.com.solutis.pagamento_service.entity.Status;
import br.com.solutis.pagamento_service.repository.PagamentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Pagamento pagamentoExistente;

    @BeforeEach
    void setUp() {
        pagamentoRepository.deleteAll();

        pagamentoExistente = new Pagamento();
        pagamentoExistente.setFkUsuario(1L);
        pagamentoExistente.setFkCurso(1L);
        pagamentoExistente.setValor(150.0);
        pagamentoExistente.setStatus(Status.valueOf("aprovado"));
        pagamentoExistente.setData(new Date());

        pagamentoExistente = pagamentoRepository.save(pagamentoExistente);
    }

    @Test
    void deveCadastrarPagamento() throws Exception {
        PagamentoRequestDto dto = new PagamentoRequestDto();
        dto.setFkUsuario(2L);
        dto.setFkCurso(2L);
        dto.setValor(200.0);
        dto.setStatus(Status.valueOf("pendente"));
        dto.setData(new Date());

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "token-fake"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor", is(200.0)))
                .andExpect(jsonPath("$.status", is("pendente")));
    }

    @Test
    void deveListarTodosPagamentos() throws Exception {
        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idPagamento", is(pagamentoExistente.getIdPagamento().intValue())));
    }

    @Test
    void deveListarPagamentoPorId() throws Exception {
        mockMvc.perform(get("/" + pagamentoExistente.getIdPagamento())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPagamento", is(pagamentoExistente.getIdPagamento().intValue())));
    }

    @Test
    void deveAtualizarPagamento() throws Exception {
        PagamentoRequestDto dto = new PagamentoRequestDto();
        dto.setFkUsuario(pagamentoExistente.getFkUsuario());
        dto.setFkCurso(pagamentoExistente.getFkCurso());
        dto.setValor(300.0);
        dto.setStatus(Status.valueOf("aprovado"));
        dto.setData(new Date());

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/" + pagamentoExistente.getIdPagamento())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor", is(300.0)))
                .andExpect(jsonPath("$.status", is("aprovado")));
    }

    @Test
    void deveDeletarPagamento() throws Exception {
        mockMvc.perform(delete("/" + pagamentoExistente.getIdPagamento()))
                .andExpect(status().isNoContent());

        // Verificar que deletou
        mockMvc.perform(get("/" + pagamentoExistente.getIdPagamento()))
                .andExpect(status().isNotFound());
    }
}
