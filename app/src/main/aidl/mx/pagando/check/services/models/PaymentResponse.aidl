package mx.pagando.check.services.models;

parcelable PaymentResponse {
    String responseCode;
    String description;
    String authCode;
    String traceNumber;
    String retrievalReference;
    String cardIssuerResponse;
    String receivingInstitutionCode;
    String terminalAddressBranch;
    String posSettlementData;
    String messageType;
    String status;
    String folio;
    boolean requireSignature;
    boolean mustReloadKeyNow;
    boolean mustReloadKeySoon;
    String ARQC;
    String ARPC;
    String xmlTlv;
    String cardBrand;
    String brand;
    String last4;
    String AID;
    String accountType;
    String employee;
    double tipAmount = 0.0;
}