package br.com.solutis.certificado_service.client;

import br.com.solutis.certificado_service.dto.pdfdto.PdfResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Service
public class PdfClient {

    private static final Logger log = LoggerFactory.getLogger(PdfClient.class);
    private final WebClient webClient;
    private final String templateId;
    private final String pdfToken;

    public PdfClient(WebClient.Builder builder,
                     @Value("${pdfmonkey.api.url}") String pdfApiUrl,
                     @Value("${pdfmonkey.template.id}") String templateId,
                     @Value("${pdfmonkey.api.token}") String pdfToken) {
        this.webClient = builder.baseUrl(pdfApiUrl).build();
        this.templateId = templateId;
        this.pdfToken = pdfToken;
    }

    public String gerarPdf(String nomeCurso, String nomeUsuario, LocalDate dataEmissao) {
        Map<String, Object> payload = Map.of(
                "nomeCurso", nomeCurso,
                "nomeUsuario", nomeUsuario,
                "dataEmissao", dataEmissao.toString()
        );

        Map<String, Object> body = Map.of(
                "document", Map.of(
                        "document_template_id", templateId,
                        "payload", payload
                )
        );

        PdfResponseDto response = null;

        try {
            String jsonResponse = webClient.post()
                    .uri("/api/v1/documents")
                    .header("Authorization", "Bearer " + pdfToken)
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res ->
                            res.bodyToMono(String.class).flatMap(errorBody -> {
                                System.err.println("Erro ao chamar PDFMonkey: " + errorBody);
                                return Mono.error(new RuntimeException("Erro ao gerar PDF: " + errorBody));
                            })
                    )
                    .bodyToMono(String.class)
                    .block();

            log.info("Resposta JSON da API PDFMonkey: {}", jsonResponse);

            ObjectMapper mapper = new ObjectMapper();
            response = mapper.readValue(jsonResponse, PdfResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da API de PDF", e);
        }

        if (response == null || response.getUrlDownload() == null) {
            throw new RuntimeException("Resposta da API de PDF inválida");
        }

        log.info("PDF gerado com sucesso. URL: {}", response.getUrlDownload());
        return response.getUrlDownload();
    }
}
