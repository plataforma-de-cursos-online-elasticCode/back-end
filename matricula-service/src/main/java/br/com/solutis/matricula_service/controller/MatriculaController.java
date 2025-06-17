package br.com.solutis.matricula_service.controller;

import br.com.solutis.matricula_service.dto.MatriculaAlunoResponseDto;
import br.com.solutis.matricula_service.dto.MatriculaCursoResponseDto;
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

        return ResponseEntity.status(201).body(response);
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

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/listar/aluno/{id}")
    public ResponseEntity<List<MatriculaAlunoResponseDto>> listarMatriculasPorAluno(@PathVariable Long id) {
        List<Matricula> matriculas = service.listarMatriculasPorAluno(id);

        if (matriculas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<MatriculaAlunoResponseDto> response = matriculas.stream()
                .map(mapper::toAlunoResponse)
                .toList();

        for (int i = 0; i <= response.size() - 1; i++) {
            String url = "/cursos/" + matriculas.get(i).getCursoId();
            Curso curso = cursoWebClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Curso.class)
                    .block();

            response.get(i).setNomeCurso(curso.getNome());
        }

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/listar/curso/{id}")
    public ResponseEntity<List<MatriculaCursoResponseDto>> listarMatriculasPorCursoId(@PathVariable Long id) {
        List<Matricula> matriculas = service.listarMatriculasPorCursoId(id);

        if (matriculas.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<MatriculaCursoResponseDto> response = matriculas.stream()
                .map(mapper::toCursoResponse)
                .toList();

        for (int i = 0; i <= response.size() - 1; i++) {
            String url = "/usuarios/" + matriculas.get(i).getAlunoId();
            Usuario aluno = usuarioWebClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Usuario.class)
                    .block();

            response.get(i).setNomeAluno(aluno.getNome());
        }

        return ResponseEntity.status(200).body(response);
    }


}
