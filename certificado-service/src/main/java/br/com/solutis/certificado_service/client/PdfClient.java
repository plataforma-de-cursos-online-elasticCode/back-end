package br.com.solutis.certificado_service.client;

import br.com.solutis.certificado_service.dto.pdfdto.PdfResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Service
public class PdfClient {

    @Value("${pdfmonkey.template.id}")
    private String templateId;

    @Value("${pdfmonkey.api.url}")
    private String pdfApiUrl;

    @Value("${pdfmonkey.api.token}")
    private String pdfToken;

    private final WebClient webClient;

    public PdfClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(pdfApiUrl).build();
    }

    public String gerarPdf(String nomeCurso, String nomeUsuario, LocalDate dataEmissao) {
        Map<String, Object> payload = Map.of(
                "nomeCurso", nomeCurso,
                "nomeUsuario", nomeUsuario,
                "dataEmissao", dataEmissao.toString()
        );

        Map<String, Object> body = Map.of(
                "document", Map.of(
                        "template", templateId,
                        "payload", payload
                )
        );

        PdfResponseDto response = webClient.post()
                .uri("/api/v1/documents")
                .header("Authorization", "Bearer " + pdfToken)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.error(new RuntimeException("Erro ao gerar PDF")))
                .bodyToMono(PdfResponseDto.class)
                .block();

        return response.getUrlDownload();
    }
}
