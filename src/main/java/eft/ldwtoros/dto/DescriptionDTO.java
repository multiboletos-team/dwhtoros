
package eft.ldwtoros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DescriptionDTO {
    @JsonProperty("zona")
    private String zona;

    @JsonProperty("seccion")
    private String seccion;

    @JsonProperty("fila")
    private String fila;

    @JsonProperty("asiento")
    private Long asiento;

    public String getZona() { return zona; }
    public String getSeccion() { return seccion; }
    public String getFila() { return fila; }
    public Long getAsiento() { return asiento; }
}
