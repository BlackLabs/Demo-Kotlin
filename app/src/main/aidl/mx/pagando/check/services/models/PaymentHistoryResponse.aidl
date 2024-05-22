package mx.pagando.check.services.models;

import java.util.List;
/**
 * Clase PaymentHistoryResponse representa la respuesta del historial de pagos.
 * Contiene listas detalladas de varios aspectos de las transacciones de pago.
 */
parcelable PaymentHistoryResponse {

    // Lista de id de transacciones
    List<String> transactionId;

    // Lista de estados del pago. Cada elemento indica si un pago fue aprobado o no.
    List<String> status;

    // Descripciones detalladas de los estados de los pagos.
    List<String> statusDescription;

    // Folios asociados con cada transacción de pago.
    List<String> folio;

    // Dispositivos utilizados para realizar las operaciones de pago.
    List<String> operationDevice;

    // Tipos de operaciones realizadas, por ejemplo, compra, reembolso, etc.
    List<String> operationType;

    // Montos de las transacciones en cada operación de pago.
    List<String> transactionAmount;

    // Tipos de tarjetas utilizadas en las transacciones, por ejemplo, débito, crédito.
    List<String> cardType;

    // Últimos cuatro dígitos de la tarjeta utilizada en cada transacción.
    List<String> lastFour;

    // Fechas y horas de cada transacción realizada.
    List<String> transactionTime;

    // Credito, debito
    List<String> paymentType;

    //subtotal de la compra
    List<String> subtotal;

    //propina sobre la compra
    List<String> tipAmount;
    // Saber si ha sido dispersado
    List<String> hasDispersion;

    boolean hasNext;

    String nextPage;
}
