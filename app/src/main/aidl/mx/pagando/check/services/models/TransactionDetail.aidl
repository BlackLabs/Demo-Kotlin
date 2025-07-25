package mx.pagando.check.services.models;

parcelable TransactionDetail {
     String status;
     String folio;
     String date;
     String deviceSerialNumber;
     String last4;
     String cardBrand;
     String cardType;
     String operationType;
     double total;
     String tipAmountWCommision;
     int numberOfPayments;
     int monthsToWait;
     String promotionType;
     String terminalCutFolio;
     String authCode;
     String AID;
     String ARQC;
     String originalFolio;
     double organizationCommisionAmount;
     double organizationCommisionPercent;
     int totalDiscounts;
     boolean hasDispersion;
     String rejectReason;
}
