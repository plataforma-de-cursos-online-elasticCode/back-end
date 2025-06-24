package br.com.solutis.certificado_service.dto.pdfdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PdfResponseDto {
    @JsonProperty("document")
    private Document document;

    public String getUrlDownload() {
        return document != null
                ? (document.downloadUrl != null ? document.downloadUrl : document.previewUrl)
                : null;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        @JsonProperty("download_url")
        private String downloadUrl;

        @JsonProperty("preview_url")
        private String previewUrl;
    }

}
