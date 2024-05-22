package mx.pagando.check.services;
import mx.pagando.check.services.models.CardBrand;
import mx.pagando.check.services.models.ErrorResponse;

interface GetCardBrandCallback {
    void onSuccessful(inout CardBrand cardBrand);
    void onError(inout ErrorResponse errorResponse);
}