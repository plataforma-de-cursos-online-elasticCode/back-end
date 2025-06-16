package br.com.solutis.matricula_service.controller;

import br.com.solutis.matricula_service.dto.MatriculaRequestDto;
import br.com.solutis.matricula_service.dto.MatriculaResponseDto;
import br.com.solutis.matricula_service.entity.Matricula;
import br.com.solutis.matricula_service.mapper.MatriculaMapper;
import br.com.solutis.matricula_service.service.MatriculaService;
import br.com.solutis.usuario_service.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService service;

    @Autowired
    private MatriculaMapper mapper;

    private final WebClient cursoWebClient;
    private final WebClient usuarioWebClient;

    @Autowired
    public MatriculaController(WebClient.Builder webClientBuilder) {
        this.cursoWebClient = webClientBuilder.baseUrl("http://localhost:8085").build();
        this.usuarioWebClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    // Esse endpoint não vai funcionar até que o serviço de curso esteja implementado.
    @PostMapping
    public ResponseEntity<MatriculaResponseDto> matricular(@RequestBody MatriculaRequestDto req) {
        Matricula entity = service.matricular(req);

        String urlCurso = "/cursos/" + req.cursoId();
        Curso curso = cursoWebClient.get()
                .uri(urlCurso)
                .retrieve()
                .bodyToMono(Curso.class)
                .block();

        String urlUsuario = "/usuarios/" + req.alunoId();
        Usuario aluno = usuarioWebClient.get()
                .uri(urlUsuario)
                .retrieve()
                .bodyToMono(Usuario.class)
                .block();

        MatriculaResponseDto response = mapper.toResponse(entity);
        response.setNomeCurso(curso.getNome());
        response.setNomeAluno(aluno.getNome());

        ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDto> buscarPorId(@PathVariable Long id) {
        Optional<Matricula> matricula = service.buscarPorId(id);
        if (matricula.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        MatriculaResponseDto response = mapper.toResponse(matricula.get());

        String urlCurso = "/cursos/" + matricula.get().getCursoId();
        Curso curso = cursoWebClient.get()
                .uri(urlCurso)
                .retrieve()
                .bodyToMono(Curso.class)
                .block();

        String urlUsuario = "/usuarios/" + matricula.get().getAlunoId();
        Usuario aluno = usuarioWebClient.get()
                .uri(urlUsuario)
                .retrieve()
                .bodyToMono(Usuario.class)
                .block();

        response.setNomeCurso(curso.getNome());
        response.setNomeAluno(aluno.getNome());

        ResponseEntity.status(201).body(response);
    }

}
