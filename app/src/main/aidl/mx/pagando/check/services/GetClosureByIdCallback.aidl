package mx.pagando.check.services;
import mx.pagando.check.services.models.closure.ClosureDetail;
import mx.pagando.check.services.models.ErrorResponse;

interface GetClosureByIdCallback {
    void onError(inout ErrorResponse errorResponse);
    void onSuccessful(inout ClosureDetail closureDetail);
}