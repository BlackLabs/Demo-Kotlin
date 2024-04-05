package mx.pagando.check.services.models;

parcelable PendingCheckOutResponse {
    List<String> folio;

    List<String> order;

    List<String> date;

    List<String> total;

    List<String> cardType;

    List<String> lastFour;

    List<String> firstSix;

    List<String> authCode;

    List<String> totalReauth;

    String totalDocks;

    boolean hasPrevPage;

    boolean hasNextPage;

    String nextPage;

}