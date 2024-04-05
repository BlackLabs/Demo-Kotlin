package mx.pagando.check.services;

interface PagandoServicesCallback {
    void onResponse(boolean error, String response);
}