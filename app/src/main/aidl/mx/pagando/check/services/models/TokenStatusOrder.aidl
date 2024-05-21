package mx.pagando.check.services.models;

parcelable TokenStatusOrder {
     String folio;
     String authCode;
     String currency;
     String cardBrand;
     String operationStatus;
     String accountType;
     String concept;
     String created_at;
     String amount;
     String last4;
     String emvType;
     boolean isRecurrentPayment;
     String operationType;
     String responseCode;
     String responseDescription;
}
