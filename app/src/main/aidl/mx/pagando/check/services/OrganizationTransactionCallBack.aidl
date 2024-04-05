package mx.pagando.check.services;
import mx.pagando.check.services.models.PaymentHistoryResponse;
import mx.pagando.check.services.models.ErrorResponse;

interface OrganizationTransactionCallBack {
    void onSuccessful(inout PaymentHistoryResponse response);
    void onError(inout ErrorResponse errorResponse);
}
