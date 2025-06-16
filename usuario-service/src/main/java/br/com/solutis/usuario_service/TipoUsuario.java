package br.com.solutis.usuario_service;

public enum TipoUsuario {

    ALUNO,
    PROFESSOR;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
