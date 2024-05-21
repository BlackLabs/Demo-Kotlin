// IAIDLColorInterface.aidl
package mx.pagando.check.services;
import mx.pagando.check.services.PagandoServicesCallback;
import mx.pagando.check.services.MakePaymentCallback;
import mx.pagando.check.services.ReadCardCallback;
import mx.pagando.check.services.GetOperationsCallback;
import mx.pagando.check.services.LoginCallback;
import mx.pagando.check.services.LogoutCallback;
import mx.pagando.check.services.InitKeysCallback;
import mx.pagando.check.services.TransactionByFolioCallback;
import mx.pagando.check.services.PrintCallback;
import mx.pagando.check.services.SignatureCallback;
import mx.pagando.check.services.PromotionsCallback;
import mx.pagando.check.services.OrganizationTransactionCallBack;
import mx.pagando.check.services.PendingCheckoutCallback;
import mx.pagando.check.services.GetOrganizationsCallback;
import mx.pagando.check.services.ChangeOrganizationCallback;
import mx.pagando.check.services.PaginateTerminalClosureCallback;
import mx.pagando.check.services.PreTerminalClosureCallback;
import mx.pagando.check.services.GenerateClosureCallback;
import mx.pagando.check.services.GetClosureByIdCallback;
import mx.pagando.check.services.DoCheckoutCallback;
import mx.pagando.check.services.SendEmailCallback;
import mx.pagando.check.services.TransactionDetailCallback;
import mx.pagando.check.services.TokenStatusCallback;
import mx.pagando.check.services.models.OrganizationInfo;
import mx.pagando.check.services.models.UserInfo;

import android.graphics.Bitmap;
// Declare any non-default types here with import statements

interface AIDLPagandoInterface {
    void init();
    void login(String email, String password, boolean sandbox, LoginCallback callback);
    void loginApi(String key, String cookie, boolean sandbox, LoginCallback callback);
    void logout(LogoutCallback callback);

    void getOperations(GetOperationsCallback callback);
    void initKeys(InitKeysCallback callback);
    void makePayment(MakePaymentCallback callback);
    void printStr(String str, int fontSize, PrintCallback callback);
    void printBitmap(in Bitmap bitmap, PrintCallback callback);
    void readCard(String amount, String operation, inout int[] positions, ReadCardCallback callback);
    void setSignature(String signature, SignatureCallback callback);
    void setPromotion(String promotionCheck, String numberOfPayments);
    void setPayLaterPromotion(String promotionCheck, String numberOfPayments, String monthsWait);
    void getPayLaterPromotions(PromotionsCallback callback);
    void getPromotions(PromotionsCallback callback);
    void setInfoOthersOperations(String folio, String employee, String notes);
    void setTipAndWaiter(String amountTip, String waiter);
    void sendEmail(String email, SendEmailCallback callback);

    void getUserOrganizations(GetOrganizationsCallback callback);
    void getOrganizationById(String organizationId, PagandoServicesCallback callback);
    void changeOrganization(String profile, ChangeOrganizationCallback callback);
    void recovertPasswordEmail(String email, PagandoServicesCallback callback);
    void operationByFolio(String folio, TransactionByFolioCallback callback);
    void organizationTransaction(String currentPage, String perPage, String search, String filter, OrganizationTransactionCallBack callback);
    void pendingCheckOuts(int perPage, int currentPage, String search, String filters, String organization, String date, PendingCheckoutCallback callback);

    // CheckoutCardLess
    void doCheckout(String reference, String amount, DoCheckoutCallback callback);
    //terminal cut
    void paginateTerminalClosure(int perPage, int currentPage, PaginateTerminalClosureCallback callback);
    void preTerminalClosure(PreTerminalClosureCallback callback);
    void generateClosure(String password, GenerateClosureCallback callback);
    void getClosureById(String password, GetClosureByIdCallback callback);
    void getTransactionDetail(String transactionId, TransactionDetailCallback callback);

    void cancelReadCard();
    OrganizationInfo getSelectedOrganization();
    UserInfo getUserInfo();
    String getAffiliation();
    void getTokenStatus(String tokenInPotency, TokenStatusCallback callback);
}