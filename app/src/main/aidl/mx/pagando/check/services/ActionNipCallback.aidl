package mx.pagando.check.services;

interface ActionNipCallback {
    void onCancel();
    void onSetPin(String pin);
}