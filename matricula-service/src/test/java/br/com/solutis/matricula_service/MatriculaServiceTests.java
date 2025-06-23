package br.com.solutis.matricula_service;

import br.com.solutis.matricula_service.dto.MatriculaRequestDto;
import br.com.solutis.matricula_service.entity.Matricula;
import br.com.solutis.matricula_service.entity.Status;
import br.com.solutis.matricula_service.mapper.MatriculaMapper;
import br.com.solutis.matricula_service.repository.MatriculaRepository;
import br.com.solutis.matricula_service.service.MatriculaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatriculaServiceTests {

    @Mock
    private MatriculaRepository repository;

    @Mock
    private MatriculaMapper mapper;

    @InjectMocks
    private MatriculaService service;

    private MatriculaRequestDto req;
    private Matricula entity;

    @BeforeEach
    void setUp() {

        req = new MatriculaRequestDto();
        req.setAlunoId(1L);
        req.setCursoId(1L);

        entity = new Matricula();
        entity.setId(1L);
        entity.setDataMatricula(LocalDate.now());
        entity.setStatus(Status.ATIVA);
        entity.setAlunoId(1L);
        entity.setCursoId(1L);

    }

    @Test
    @DisplayName("Deve criar uma nova matrícula com dados válidos e retornar a matrícula criada")
    void criarMatricula_dadosValidos_retornaNovaMatricula() {
        when(mapper.toEntity(req)).thenReturn(entity);
        when(repository.save(any(Matricula.class))).thenReturn(entity);

        Matricula savedEntity = service.matricular(req);

        assertNotNull(savedEntity);
        assertEquals(1L, savedEntity.getAlunoId());
        assertEquals(1L, savedEntity.getCursoId());
        assertEquals(Status.ATIVA, savedEntity.getStatus());
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toEntity(req);
    }

    @Test
    @DisplayName("Deve buscar uma matrícula por id e retornar a matrícula correta")
    void buscarMatriculaPorId_idValido_retornaMatricula() {
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(entity));

        var foundEntity = service.buscarPorId(1L);

        assertNotNull(foundEntity);
        assertEquals(1L, foundEntity.get().getAlunoId());
        verify(repository, times(1)).findById(1L);
    }

}
