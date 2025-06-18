package br.com.solutis.certificado_service.repository;

import br.com.solutis.certificado_service.entity.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
}
