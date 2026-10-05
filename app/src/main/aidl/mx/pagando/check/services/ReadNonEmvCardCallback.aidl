package mx.pagando.check.services;
import mx.pagando.check.services.models.ErrorResponse;

/**
 * Callback de readMagneticCard y readNfcCard (tarjetas de membresía, lealtad, control de acceso).
 *
 * onSuccessful(cardData): JSON con lo leído de la tarjeta.
 *   readMagneticCard -> {"track1","track2","track3","cardNo","expiryDate","serviceCode"}
 *   readNfcCard      -> {"uid","cardType","cardTypeName","resetData"} (bytes en hexadecimal)
 */
interface ReadNonEmvCardCallback {
    void onSuccessful(String cardData);
    void onError(inout ErrorResponse errorResponse);
}
