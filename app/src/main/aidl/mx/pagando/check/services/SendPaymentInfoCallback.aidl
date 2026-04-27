// SendPaymentInfoCallback.aidl
package mx.pagando.check.services;

interface SendPaymentInfoCallback {
    void onSuccessful();
    void onError();
}