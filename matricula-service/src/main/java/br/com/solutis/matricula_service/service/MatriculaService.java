package br.com.solutis.matricula_service.service;

import br.com.solutis.matricula_service.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository repository;

}
