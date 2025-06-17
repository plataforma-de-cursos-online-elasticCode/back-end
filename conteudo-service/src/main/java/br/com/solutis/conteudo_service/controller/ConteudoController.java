package br.com.solutis.conteudo_service.controller;

import br.com.solutis.conteudo_service.dto.ConteudoRequestDto;
import br.com.solutis.conteudo_service.entity.Conteudo;
import br.com.solutis.conteudo_service.service.ConteudoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conteudos")
public class ConteudoController {

    @Autowired
    private ConteudoService service;

    @PostMapping
    public ResponseEntity<Conteudo> cadastrarConteudo(@RequestBody ConteudoRequestDto request) {
        Conteudo novo = service.cadastrarConteudo(request);
        return ResponseEntity.status(201).body(novo);
    }

    @GetMapping
    public ResponseEntity<List<Conteudo>> listarTodosConteudos() {
        return ResponseEntity.status(200).body(service.listarTodosConteudos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conteudo> buscarConteudoPorId(@PathVariable Long id) {
        Conteudo response = service.buscarConteudoPorId(id);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConteudoPorId(@PathVariable Long id) {
        service.deletarConteudoPorId(id);

        return ResponseEntity.status(204).build();
    }
}
