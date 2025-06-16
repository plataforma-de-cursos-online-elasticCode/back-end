package br.com.solutis.pagamento_service.controller;

import br.com.solutis.pagamento_service.dto.PagamentoRequestDto;
import br.com.solutis.pagamento_service.dto.PagamentoResponseDto;
import br.com.solutis.pagamento_service.entity.Pagamento;
import br.com.solutis.pagamento_service.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    @Autowired
    private PagamentoService service;

    // CREATE
    @PostMapping
    public ResponseEntity<PagamentoResponseDto> cadastrar(@RequestBody PagamentoRequestDto dto){
        return ResponseEntity.status(201).body(service.cadastrar(dto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDto>> listar(){
        return ResponseEntity.status(200).body(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDto> listarPorId(@PathVariable Long id){
        return ResponseEntity.status(200).body(service.listarPorId(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Pagamento> atualizar(
            @PathVariable Long id,
            @RequestBody PagamentoRequestDto dto
    ){
        return ResponseEntity.status(200).body(service.atualizar(id, dto));
    }

    // DELETE
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
