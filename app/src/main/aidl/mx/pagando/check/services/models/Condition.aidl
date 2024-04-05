package mx.pagando.check.services.models;

parcelable Condition {
    int time;
    float minAmount;
    String promotionName;
    String promotionCheck ;
    float monthlyPayment;
    float rate;
    int[] monthsWait;
}