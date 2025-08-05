package eft.ldwtoros.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "error_log")
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 255)
    private String endpoint;

    @Column(length = 50)
    private String httpMethod;

    @Column(length = 255)
    private String orderId;

    @Column(length = 1000)
    private String errorMessage;

    @Column(length = 2000)
    private String stackTrace;

    // 🔹 Constructor vacío
    public ErrorLog() {}

 // Constructor que acepta Strings
    public ErrorLog(String endpoint, String httpMethod, String orderId, String errorMessage, String stackTrace) {
        this.timestamp = LocalDateTime.now();
        this.endpoint = endpoint;
        this.httpMethod = httpMethod;
        this.orderId = orderId;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    // Sobrecarga para aceptar Long
    public ErrorLog(String endpoint, String httpMethod, Long orderId, String errorMessage, String stackTrace) {
        this(endpoint, httpMethod,
             orderId != null ? orderId.toString() : null,  // Conversión aquí
             errorMessage,
             stackTrace);
    }


    // Getters y Setters
    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getStackTrace() { return stackTrace; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
}
