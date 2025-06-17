package br.com.solutis.matricula_service.service;

import br.com.solutis.matricula_service.entity.Matricula;
import br.com.solutis.matricula_service.repository.MatriculaRepository;
import br.com.solutis.usuario_service.entity.Usuario;
import br.com.solutis.usuario_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EMailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private MatriculaRepository matriculaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private CursoRepository cursoRepo;

    private final WebClient cursoWebClient;
    private final WebClient usuarioWebClient;

    @Autowired
    public EMailService(WebClient.Builder webClientBuilder) {
        this.cursoWebClient = webClientBuilder.baseUrl("http://localhost:8085").build();
        this.usuarioWebClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public void enviarEmailConfirmacao(Matricula entity) {
        SimpleMailMessage message = new SimpleMailMessage();

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

        message.setTo(aluno.getEmail());
        message.setSubject("Confirmação de Matrícula");
        message.setText("Olá " + aluno.getNome() + ",\n\n" +
                "Sua matrícula no curso " + curso.getNome() + " foi realizada com sucesso!\n\n" +
                "Obrigado por se matricular conosco!");

        sender.send(message);

        System.out.println("📤 E-mail de confirmação enviado para: " + aluno.getEmail());
    }

}
