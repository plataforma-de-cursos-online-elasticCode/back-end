package br.com.solutis.avaliacao_service.service;

import br.com.solutis.avaliacao_service.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository repository;

}
