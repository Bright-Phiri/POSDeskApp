package posdeskapp.services;

import javafx.concurrent.Task;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.concurrent.ScheduledService;
import posdeskapp.api.ApiClient;
import posdeskapp.api.HttpResponseResult;
import posdeskapp.models.Invoice;
import posdeskapp.models.InvoiceHeader;
import posdeskapp.models.LineItem;
import posdeskapp.models.SalesInvoice;
import posdeskapp.utils.DbHelper;
import posdeskapp.utils.POSHelper;

public class TransmissionService extends ScheduledService<Void> {

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    // Fetch untransmitted invoices
                    List<Invoice> untransmittedInvoices = DbHelper.getUntransmittedInvoices();
                    String siteId = DbHelper.fetchTerminalSiteId();

                    for (Invoice invoice : untransmittedInvoices) {
                        try {
                            // Fetch line items for the current invoice
                            List<LineItem> lineItems = DbHelper.getInvoiceLineItems(invoice.getInvoiceNumber());

                            InvoiceHeader invoiceHeader = new InvoiceHeader(
                                    invoice.getInvoiceNumber(),
                                    invoice.getInvoiceDateTime(),
                                    invoice.getSellerTin(),
                                    invoice.getBuyerTin(),
                                    "",
                                    siteId,
                                    1, // GlobalConfigVersion (example static value)
                                    1, // TaxpayerConfigVersion (example static value)
                                    1  // TerminalConfigVersion (example static value)
                            );

                            SalesInvoice invoiceRequest = POSHelper.createInvoiceRequest(
                                    invoiceHeader,
                                    lineItems,
                                    invoice.getInvoiceTotal(),
                                    invoice.getTotalVat()
                            );

                            Gson gson = new GsonBuilder()
                                    .excludeFieldsWithoutExposeAnnotation()
                                    .create();
                            String invoicePayload = gson.toJson(invoiceRequest);

                            // Submit sales transaction
                            HttpResponseResult httpResponseResult = ApiClient.submitSalesTransaction(invoicePayload);

                            if (httpResponseResult.getStatusCode() == 200) {
                                DbHelper.markInvoiceAsProcessed(invoice.getInvoiceNumber());
                            } else {
                                System.err.println("Failed to transmit invoice: " + invoice.getInvoiceNumber());
                            }
                        } catch (Exception ex) {
                            System.err.println("Error processing invoice " + invoice.getInvoiceNumber() + ": " + ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Error in TransmissionService: " + ex.getMessage());
                }
                return null;
            }
        };
    }
}
