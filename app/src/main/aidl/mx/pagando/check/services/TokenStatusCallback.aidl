package mx.pagando.check.services;
import mx.pagando.check.services.models.TokenStatusOrder;
import mx.pagando.check.services.models.ErrorResponse;

interface TokenStatusCallback {
    void onSuccessful(String status, inout TokenStatusOrder order);
    void onError(inout ErrorResponse errorResponse);
}