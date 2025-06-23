package br.com.solutis.avaliacao_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    private Double nota;
    private String comentario;

}
