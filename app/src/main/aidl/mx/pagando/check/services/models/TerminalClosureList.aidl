package mx.pagando.check.services.models;
import mx.pagando.check.services.models.SimpleClosureInfo;

parcelable TerminalClosureList {
    List<SimpleClosureInfo> docs;
    boolean hasNextPage;
    boolean hasPrevPage;
    int limit;
    int page;
    int pagingCounter;
    int totalDocs;
    int totalPages;
}