package mx.pagando.check.services.models.closure;

import mx.pagando.check.services.models.closure.SaleDetail;
import mx.pagando.check.services.models.closure.SalesTipDetail;
import mx.pagando.check.services.models.closure.TipDetail;
import mx.pagando.check.services.models.closure.SaleHotelCars;
import mx.pagando.check.services.models.closure.SalesWithPromo;
import mx.pagando.check.services.models.closure.SalesTypePay;
import mx.pagando.check.services.models.closure.TerminalCutDetail;


parcelable ClosureDetail {
    SaleDetail salesDetails;
    SalesTipDetail salesTipDetails;
    List<TipDetail> tipDetails;
    SaleHotelCars salesHotelsCars;
    SalesWithPromo salesWithPromo;
    List<SalesTypePay> salesTypePay;
    String giro ;
    boolean showTip;
    boolean showDetailTip;
    boolean showHotelsRent;
    boolean showSalesWithPromo;
    boolean showSalesTypePay;
    String fromDate;
    String toDate;
    String folio ;
    List<TerminalCutDetail> terminalCutDetail;
    String serialNoTerminal ;
    double controldouble;
    boolean withDetail;
}
