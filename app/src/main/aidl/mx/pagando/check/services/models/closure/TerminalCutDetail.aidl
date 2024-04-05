package mx.pagando.check.services.models.closure;

import mx.pagando.check.services.models.closure.TerminalCutDetailTransactions;

parcelable TerminalCutDetail{
    String employee;
    boolean hasWaiter;
    List<TerminalCutDetailTransactions> transactions;
    double totalSales;
    double totalTips;
}

