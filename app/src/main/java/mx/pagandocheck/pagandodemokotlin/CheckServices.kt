package mx.pagandocheck.pagandodemokotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteException
import android.util.Log
import androidx.activity.ComponentActivity
import mx.pagando.check.services.AIDLPagandoInterface
import mx.pagando.check.services.ChangeOrganizationCallback
import mx.pagando.check.services.DoCheckoutCallback
import mx.pagando.check.services.GenerateClosureCallback
import mx.pagando.check.services.GetCardBrandCallback
import mx.pagando.check.services.GetClosureByIdCallback
import mx.pagando.check.services.GetOperationsCallback
import mx.pagando.check.services.GetOrganizationsCallback
import mx.pagando.check.services.InitKeysCallback
import mx.pagando.check.services.LoginCallback
import mx.pagando.check.services.LogoutCallback
import mx.pagando.check.services.MakePaymentCallback
import mx.pagando.check.services.OrganizationTransactionCallBack
import mx.pagando.check.services.PaginateTerminalClosureCallback
import mx.pagando.check.services.PendingCheckoutCallback
import mx.pagando.check.services.PreTerminalClosureCallback
import mx.pagando.check.services.PrintCallback
import mx.pagando.check.services.PromotionsCallback
import mx.pagando.check.services.ReadCardCallback
import mx.pagando.check.services.SignatureCallback
import mx.pagando.check.services.TokenStatusCallback
import mx.pagando.check.services.TransactionByFolioCallback
import mx.pagando.check.services.TransactionDetailCallback
import mx.pagando.check.services.models.ErrorResponse

class CheckServices(context: Context) {
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            pagandoService = AIDLPagandoInterface.Stub.asInterface(iBinder)
            pagandoService!!.init()
            Log.d("HardwareServices", "Remote config Service Connected!!")
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            pagandoService = null
            Log.d("HardwareServices", "Remote config Service Disconnected!!")
            context.bindService(
                Intent("mx.pagando.check.services.AIDLPagandoService")
                    .setPackage("mx.pagando.check.services"),
                this,
                ComponentActivity.BIND_AUTO_CREATE
            )
        }
    }
    fun responseCode(message: String?): ErrorResponse {
        val errorResponse = ErrorResponse()
        errorResponse.message = message
        errorResponse.code = "-5555"
        return errorResponse
    }
    init {
        context.bindService(
            Intent("mx.pagando.check.services.AIDLPagandoService")
                .setPackage("mx.pagando.check.services"),
            mConnection,
            ComponentActivity.BIND_AUTO_CREATE
        )
    }

    fun unbindService() {
        App.appContext.unbindService(mConnection)
    }

    var pagandoService: AIDLPagandoInterface? = null

    companion object {
        var service: CheckServices? = null
        fun getInstance(context: Context): CheckServices {
            if (service == null)
                service = CheckServices(context)
            return service!!
        }
    }


    /**
     * Permite al usuario autenticarse en el sistema Pagando Check.
     *
     * @param key llave de la aplicación.
     * @param cookie
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun loginApi(key: String, cookie: String, sandbox: Boolean, callback: LoginCallback.Stub) {
        if (pagandoService != null)
            pagandoService!!.loginApi(key, cookie,false, callback)
        else
            callback.onError(responseCode("no se pudo conectar al servicio... LogInAPI"))
    }

    /**
     * Permite al usuario cerrar sessión en el sistema Pagando Check.
     *
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun logout(callback: LogoutCallback.Stub) {
        if (pagandoService != null)
            pagandoService!!.logout(callback)
        else
            callback.onError(responseCode("no se pudo conectar al servicio... LogOut"))
    }

    /**
     * Recupera una lista de todas las organizaciones asociadas al usuario autenticado.
     *
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun getOrganizations(callback: GetOrganizationsCallback) {
        if (pagandoService != null)
            pagandoService!!.getUserOrganizations(callback)
        else
            callback.onError(responseCode("no se pudo conectar al servicio... getOrganizations"))
    }

    /**
     * Cambia la organización activa en la terminal a la especificada por el ID de perfil.
     *
     * @param profile El ID del perfil de la organización a la cual se cambiará.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun changeOrganization(profile: String?, callback: ChangeOrganizationCallback) {
        if (pagandoService != null) {
            pagandoService!!.changeOrganization(profile, callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... changeOrganization"))
        }
    }

    /**
     * Obtiene un historial de transacciones para la organización actual basado en criterios de búsqueda y filtros.
     *
     * @param currentPage La página actual de resultados.
     * @param perPage La cantidad de resultados por página.
     * @param search El término de búsqueda para filtrar transacciones.
     * @param filters Criterios adicionales de filtrado.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun organizationTransaction(
        currentPage: String?,
        perPage: String?,
        search: String?,
        filters: String?,
        callback: OrganizationTransactionCallBack.Stub
    ) {
        if (pagandoService != null) {
            pagandoService!!.organizationTransaction(
                currentPage,
                perPage,
                search,
                filters,
                callback
            )
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... organizationTransaction"))
        }
    }

    /**
     * Recupera una lista de operaciones o servicios disponibles que la terminal de Pagando Check puede realizar.
     *
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun getOperations(callback: GetOperationsCallback.Stub) {
        if (pagandoService != null) {
            pagandoService!!.getOperations(callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... getOperations"))
        }
    }

    /**
     * Envía la firma digital del cliente a los servicios de Pagando Check como parte de la finalización de una transacción.
     *
     * @param signature La firma del cliente codificada en base64.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun setSignature(signature: String?, callback: SignatureCallback.Stub) {
        if (pagandoService != null) {
            pagandoService!!.setSignature(signature, callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... setSignature"))
        }
    }

    /**
     * Imprime un bitmap en la terminal de Pagando Check.
     *
     * @param bitmap El bitmap a imprimir, codificado en base64.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun printBitmap(bitmap: Bitmap?, callback: PrintCallback.Stub) {
        if (pagandoService != null) {
            pagandoService!!.printBitmap(bitmap, callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... printBitmap"))
        }
    }

    /**
     * Imprime texto formateado en la terminal de Pagando Check.
     *
     * @param string La cadena de texto a imprimir.
     * @param fontSize El tamaño de la fuente del texto.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun printStr(string: String?, fontSize: Int, callback: PrintCallback.Stub) {
        if (pagandoService != null) {
            pagandoService!!.printStr(string, fontSize, callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... printString"))
        }
    }

    /**
     * Obtiene información sobre las promociones disponibles para pagos posteriores en Pagando Check.
     *
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun getPayLaterPromotions(callback: PromotionsCallback) {
        if (pagandoService != null) {
            pagandoService!!.getPayLaterPromotions(callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... getPayLaterPromotions"))
        }
    }

    /**
     * Recupera información sobre las promociones disponibles en Pagando Check.
     *
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun getPromotions(callback: PromotionsCallback) {
        if (pagandoService != null) {
            pagandoService!!.getPromotions(callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... getPromotions"))
        }
    }

    /**
     * Busca una transacción específica en Pagando Check usando el número de folio.
     *
     * @param folio El número de folio de la transacción a buscar.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun transactionByFolio(folio: String?, callback: TransactionByFolioCallback) {
        if (pagandoService != null) {
            pagandoService!!.operationByFolio(folio, callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... transactionByFolio"))
        }
    }

    /**
     * Recupera una lista de checkouts pendientes basados en los parámetros proporcionados.
     *
     * @param perPage Número de checkouts por página.
     * @param currentPage Página actual de resultados.
     * @param search Campo de búsqueda para filtrar los checkouts.
     * @param filters Filtros adicionales para aplicar a la búsqueda.
     * @param organization ID de la organización cuyos checkouts se están recuperando.
     * @param date Fecha de las transacciones a recuperar.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun getPendingCheckouts(
        perPage: Int,
        currentPage: Int,
        search: String?,
        filters: String?,
        organization: String?,
        date: String?,
        callback: PendingCheckoutCallback
    ) {
        if (pagandoService != null) {
            pagandoService!!.pendingCheckOuts(
                perPage,
                currentPage,
                search,
                filters,
                organization,
                date,
                callback
            )
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... getPendingCheckouts"))
        }
    }

    fun doCheckout(reference: String?, amount: String?, callback: DoCheckoutCallback) {
        if (pagandoService != null) {
            pagandoService!!.doCheckout(reference, amount, callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... doCheckout"))
        }
    }

    /**
     * Lee la información de una tarjeta a través de la terminal de Pagando Check.
     *
     * @param nipPagandoView La vista utilizada para ingresar y procesar el NIP de la tarjeta.
     * @param amount El monto de la transacción para la cual se está leyendo la tarjeta.
     * @param operation La operación específica que se realizará con los datos de la tarjeta leída.
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun readCard(
        nipPagandoView: NipPagandoView,
        amount: String?,
        operation: String?,
        callback: ReadCardCallback.Stub
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (pagandoService != null) {
                try {
                    pagandoService!!.readCard(
                        amount,
                        operation,
                        nipPagandoView.getPinLayout(),
                        callback
                    )
                } catch (e: RemoteException) {
                    throw RuntimeException(e)
                }
            } else {
                try {
                    callback.onError(responseCode("no se pudo conectar al servicio... readCard"))
                } catch (e: RemoteException) {
                    throw RuntimeException(e)
                }
            }
        }, 50)
    }
    /**
     * Inicia el proceso de pago utilizando los datos de una tarjeta previamente leída en Pagando Check.
     *
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun makePayment(callback: MakePaymentCallback.Stub) {
        if (pagandoService != null) {
            pagandoService!!.makePayment(callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... makePayment"))
        }
    }
    /**
     * Inicializa las llaves de seguridad para asegurar la comunicación entre la terminal y los servicios de Pagando Check.
     *
     * @param callback El callback que manejará la respuesta exitosa o el error de la operación.
     */
    fun initKeys(callback: InitKeysCallback.Stub) {
        if (pagandoService != null) {
            pagandoService!!.initKeys(callback)
        } else {
            callback.onError(responseCode("no se pudo conectar al servicio... initKeys"))
        }
    }
    fun getTerminalClosurePage(
        perPage: Int,
        currentPage: Int,
        callback: PaginateTerminalClosureCallback.Stub?
    ) {
        try {
            if (pagandoService != null) {
                pagandoService!!.paginateTerminalClosure(perPage, currentPage, callback)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun preTerminalClosure(callback: PreTerminalClosureCallback.Stub?) {
        try {
            if (pagandoService != null) {
                pagandoService!!.preTerminalClosure(callback)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun generateClosure(password: String?, callback: GenerateClosureCallback.Stub?) {
        try {
            if (pagandoService != null) {
                pagandoService!!.generateClosure(password, callback)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getClosureById(id: String?, callback: GetClosureByIdCallback.Stub?) {
        try {
            if (pagandoService != null) {
                pagandoService!!.getClosureById(id, callback)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getTransactionDetail(transactionId: String,  callback: TransactionDetailCallback) {
        if (pagandoService != null)
            pagandoService!!.getTransactionDetail(transactionId ,callback)
        else
            callback.onError(responseCode("no se pudo conectar al servicio... getTransactionDetail"))
    }
    fun getTokenInPotency(
        token: String,
        callback: TokenStatusCallback.Stub
    ) {
        pagandoService?.getTokenStatus(token, callback)
    }
    fun getCardBrand(callback: GetCardBrandCallback.Stub) {
        try {
            if (pagandoService != null)
                pagandoService!!.getCardBrand(callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun cancelCardRead(){
        pagandoService?.cancelReadCard()
    }
}