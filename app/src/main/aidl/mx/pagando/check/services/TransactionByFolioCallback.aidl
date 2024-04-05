package mx.pagando.check.services;
import mx.pagando.check.services.models.TransactionFolioResponse;
import mx.pagando.check.services.models.ErrorResponse;

interface TransactionByFolioCallback {
    void onSuccessful(inout TransactionFolioResponse paymentResponse);
    void onError(inout ErrorResponse errorResponse);
}