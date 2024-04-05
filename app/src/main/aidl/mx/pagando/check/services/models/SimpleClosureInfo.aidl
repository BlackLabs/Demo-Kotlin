package mx.pagando.check.services.models;
import mx.pagando.check.services.models.SimpleSaleDetail;

parcelable SimpleClosureInfo {
   String _id;
   String toDate;
   SimpleSaleDetail saleDetails;
   double totalToShow;
}