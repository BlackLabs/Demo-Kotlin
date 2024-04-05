package mx.pagando.check.services;
import mx.pagando.check.services.models.ErrorResponse;

interface InitKeysCallback {
    void onSuccessful();
    void onError(inout ErrorResponse errorResponse);
}