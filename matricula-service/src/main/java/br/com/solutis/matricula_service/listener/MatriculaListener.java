package br.com.solutis.matricula_service.listener;

import br.com.solutis.matricula_service.entity.Matricula;
import br.com.solutis.matricula_service.service.EMailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatriculaListener {

    @Autowired
    private EMailService service;

    @RabbitListener(queues = "matricula.nova")
    public void ouvirMatriculaConfirmada(Matricula entity) {
        System.out.println("🔔 Matrícula do aluno de id " + entity.getAlunoId() + " no curso de id " + entity.getCursoId() + " foi concluída com sucesso.");
        service.enviarEmailConfirmacao(entity);
    }

}
