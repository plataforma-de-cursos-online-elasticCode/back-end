package br.com.solutis.curso_service.controller;

import br.com.solutis.curso_service.dto.CursoRequestDto;
import br.com.solutis.curso_service.dto.CursoResponseDto;
import br.com.solutis.curso_service.entity.Curso;
import br.com.solutis.curso_service.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<CursoResponseDto> cadastrarCurso(@RequestBody CursoRequestDto request) {

        try {

            CursoResponseDto response = service.cadastrarCurso(request);
            return ResponseEntity.status(200).body(response);

        } catch (DataIntegrityViolationException e) {

            return ResponseEntity.status(409).build();

        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ALUNO')")
    public ResponseEntity<List<CursoResponseDto>> listarTodosCursos() {
        return ResponseEntity.status(200).body(service.listarTodosCursos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ALUNO')")
    public ResponseEntity<CursoResponseDto> buscarCursoPorId(@PathVariable Long id) {
        CursoResponseDto response = service.buscarCursoPorId(id);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/buscarPorNome")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ALUNO')")
    public ResponseEntity<CursoResponseDto> buscarCursoPorNome(@RequestParam String nome) {
        CursoResponseDto response = service.buscarCursoPorNome(nome);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("deletar/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> deletarCursoPorId(@PathVariable Long id) {
        service.deletarCursoPorId(id);

        return ResponseEntity.status(204).build();
    }

    @PutMapping("atualizar/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Curso> atualizarCursoPorId(@PathVariable Long id, @RequestBody CursoRequestDto request) {
        Curso cursoAtualizado = service.atualizarCursoPorId(id, request);

        return ResponseEntity.status(200).body(cursoAtualizado);
    }
}
