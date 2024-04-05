package mx.pagando.check.services;
import mx.pagando.check.services.models.PaymentResponse;
import mx.pagando.check.services.models.ErrorResponse;

interface MakePaymentCallback {
    void onError(inout ErrorResponse errorResponse);
    void onPaymentSuccess(inout PaymentResponse paymentResponse);
    void onSignatureRequired();
}