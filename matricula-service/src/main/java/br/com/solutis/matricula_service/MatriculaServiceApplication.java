package br.com.solutis.matricula_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
		"br.com.solutis.matricula_service.entity",
		"br.com.solutis.usuario_service.entity",
		"br.com.solutis.usuario_service.entity.usuario",
		"br.com.solutis.curso_service.entity",
		"br.com.solutis.curso_service.entity.curso"
})
@EnableJpaRepositories(basePackages = {
		"br.com.solutis.matricula_service.repository",
		"br.com.solutis.usuario_service.repository",
		"br.com.solutis.curso_service.repository"
})
public class MatriculaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatriculaServiceApplication.class, args);
	}

}
