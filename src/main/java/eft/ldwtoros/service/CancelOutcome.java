package eft.ldwtoros.service;

public enum CancelOutcome {
    CANCELLED,          // Se canceló ahora
    ALREADY_CANCELLED,  // Ya estaba cancelado
    NOT_FOUND           // No existe ese asiento en la orden
}
