package mx.pagando.check.services;
import mx.pagando.check.services.models.TransactionDetail;
import mx.pagando.check.services.models.ErrorResponse;

interface TransactionDetailCallback {
    void onSuccessful(inout TransactionDetail paymentResponse);
    void onError(inout ErrorResponse errorResponse);
}