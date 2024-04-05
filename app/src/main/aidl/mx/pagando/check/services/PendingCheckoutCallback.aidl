package mx.pagando.check.services;
import mx.pagando.check.services.models.PendingCheckOutResponse;
import mx.pagando.check.services.models.ErrorResponse;

interface PendingCheckoutCallback {
    void onSuccessful(inout PendingCheckOutResponse response);
    void onError(inout ErrorResponse errorResponse);
}
