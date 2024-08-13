package mx.pagando.check.services.models;

parcelable PreTerminalClosure {
    double totalTransactions;
    double totalSales;
    double totalMSI;
    double totalCancellations;
    double totalRefund;
    String fromDate;
    String toDate;
}