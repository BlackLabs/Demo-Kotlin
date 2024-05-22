package mx.pagando.check.services;
import mx.pagando.check.services.models.Promotion;
import mx.pagando.check.services.models.ErrorResponse;

interface PromotionsCallback {
    void onError(inout ErrorResponse errorResponse);
    void onSuccessful(inout List<Promotion> promotions);
}