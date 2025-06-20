package br.com.solutis.curso_service.controller;

import br.com.solutis.curso_service.dto.CursoRequestDto;
import br.com.solutis.curso_service.dto.CursoResponseDto;
import br.com.solutis.curso_service.entity.Curso;
import br.com.solutis.curso_service.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

    @PostMapping
    public ResponseEntity<CursoResponseDto> cadastrarCurso(@RequestBody CursoRequestDto request) {

        try {

            CursoResponseDto response = service.cadastrarCurso(request);
            return ResponseEntity.status(200).body(response);

        } catch (DataIntegrityViolationException e) {

            return ResponseEntity.status(409).build();

        }
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> listarTodosCursos() {
        return ResponseEntity.status(200).body(service.listarTodosCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> buscarCursoPorId(@PathVariable Long id) {
        CursoResponseDto response = service.buscarCursoPorId(id);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/buscarPorNome")
    public ResponseEntity<CursoResponseDto> buscarCursoPorNome(@RequestParam String nome) {
        CursoResponseDto response = service.buscarCursoPorNome(nome);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletarCursoPorId(@PathVariable Long id) {
        service.deletarCursoPorId(id);

        return ResponseEntity.status(204).build();
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Curso> atualizarCursoPorId(@PathVariable Long id, @RequestBody CursoRequestDto request) {
        return ResponseEntity.status(200).body(service.atualizarCursoPorId(id, request));
    }
}
