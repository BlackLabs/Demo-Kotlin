package mx.pagando.check.hardware;
import mx.pagando.check.hardware.SelectedAppCallback;

interface PagandoProccessCardCallback {
    void onSuccess(String decodedData, int type, String cardNo);
    void onError(String error);
    void onMessage(String message);
    void onActionNip(String message);
    void onApplicationSelection(inout String[] message, in SelectedAppCallback callback );
}