package mx.pagando.check.services;
import mx.pagando.check.services.models.PaymentResponse;
import mx.pagando.check.services.models.ErrorResponse;

interface DoCheckoutCallback {
    void onSuccessful(inout PaymentResponse paymentResponse);
    void onError(inout ErrorResponse errorResponse);
}