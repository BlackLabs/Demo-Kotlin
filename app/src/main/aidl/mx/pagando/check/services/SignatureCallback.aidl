package mx.pagando.check.services;
import mx.pagando.check.services.models.Promotion;
import mx.pagando.check.services.models.PaymentResponse;
import mx.pagando.check.services.models.ErrorResponse;

interface SignatureCallback {
    void onSuccessful(inout PaymentResponse paymentResponse);
    void onError(inout ErrorResponse errorResponse);
}