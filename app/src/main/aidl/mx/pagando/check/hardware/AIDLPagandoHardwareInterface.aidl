// IAIDLColorInterface.aidl
package mx.pagando.check.hardware;
import mx.pagando.check.hardware.PagandoHardwareCallback;
import mx.pagando.check.hardware.PagandoProccessCardCallback;
import android.graphics.Bitmap;
// Declare any non-default types here with import statements

interface AIDLPagandoHardwareInterface {
     void printBitmap(in Bitmap bitmap, PagandoHardwareCallback callback);
     void printStr(String str, int fontSize, PagandoHardwareCallback callback);
     void sendKsn(int KeyId,  in byte[] Ipek,  in byte[] Ksn, PagandoHardwareCallback callback);
     void proccessCard(String currentAmount, int mWorkKey, inout int[] positions, PagandoProccessCardCallback callback);
     void cancelProccessCard();
     String getSerialNo();
     void init();
}