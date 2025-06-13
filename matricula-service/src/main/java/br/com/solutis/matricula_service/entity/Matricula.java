package br.com.solutis.matricula_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataMatricula;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

}
