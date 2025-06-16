package br.com.solutis.usuario_service.entity;

import br.com.solutis.usuario_service.TipoUsuario;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private TipoUsuario tipoUsuario;
}
