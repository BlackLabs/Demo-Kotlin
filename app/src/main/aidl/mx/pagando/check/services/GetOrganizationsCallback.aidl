package mx.pagando.check.services;
import mx.pagando.check.services.models.GetOrganizationsResponse;
import mx.pagando.check.services.models.ErrorResponse;

interface GetOrganizationsCallback {
    void onSuccessful(inout GetOrganizationsResponse GetOrganizationsResponse);
    void onError(inout ErrorResponse errorResponse);
}
