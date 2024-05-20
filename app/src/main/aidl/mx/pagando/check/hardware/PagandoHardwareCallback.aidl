package mx.pagando.check.hardware;

interface PagandoHardwareCallback {
    void onSuccess(String folio);
    void onError(String error);
}