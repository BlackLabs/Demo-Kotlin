package mx.pagando.check.services;
import mx.pagando.check.services.models.ErrorResponse;

interface GetOperationsCallback {
    void onSuccessful(inout List<String> terminalOperations);
    void onError(inout ErrorResponse errorResponse);
}