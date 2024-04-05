package mx.pagando.check.services;
import mx.pagando.check.services.models.TerminalClosureList;
import mx.pagando.check.services.models.ErrorResponse;

interface PaginateTerminalClosureCallback {
    void onError(inout ErrorResponse errorResponse);
    void onSuccessful(inout TerminalClosureList preTerminalCut);
}