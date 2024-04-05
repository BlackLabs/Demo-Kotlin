package mx.pagando.check.services;
import mx.pagando.check.services.models.PreTerminalClosure;
import mx.pagando.check.services.models.ErrorResponse;

interface PreTerminalClosureCallback {
    void onError(inout ErrorResponse errorResponse);
    void onSuccessful(inout PreTerminalClosure terminalClosure);
}