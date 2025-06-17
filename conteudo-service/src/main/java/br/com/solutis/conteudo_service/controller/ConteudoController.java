package br.com.solutis.conteudo_service.controller;

import br.com.solutis.conteudo_service.dto.ConteudoRequestDto;
import br.com.solutis.conteudo_service.entity.Conteudo;
import br.com.solutis.conteudo_service.service.ConteudoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConteudoController {

    @Autowired
    private ConteudoService service;

    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Conteudo> cadastrarConteudo(@RequestBody ConteudoRequestDto request) {
        Conteudo novo = service.cadastrarConteudo(request);
        return ResponseEntity.status(201).body(novo);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ALUNO')")
    public ResponseEntity<List<Conteudo>> listarTodosConteudos() {
        return ResponseEntity.status(200).body(service.listarTodosConteudos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ALUNO')")
    public ResponseEntity<Conteudo> buscarConteudoPorId(@PathVariable Long id) {
        Conteudo response = service.buscarConteudoPorId(id);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> deletarConteudoPorId(@PathVariable Long id) {
        service.deletarConteudoPorId(id);

        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Conteudo> atualizarConteudo(@PathVariable Long id, @RequestBody ConteudoRequestDto request) {
        Conteudo conteudoAtualizado = service.atualizarConteudoPorId(id, request);

        return ResponseEntity.status(200).body(conteudoAtualizado);
    }
}
