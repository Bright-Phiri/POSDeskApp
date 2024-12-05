package posdeskapp.services;

import javafx.concurrent.Task;
import javafx.concurrent.ScheduledService;
import javafx.util.Duration;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
                // Fetch untransmitted invoices
                List<Invoice> untransmittedInvoices = DbHelper.getUntransmittedInvoices();
                String siteId = DbHelper.fetchTerminalSiteId();
                System.out.println("Begin Transmission");

                untransmittedInvoices.forEach(invoice -> {
                    System.out.println("Processing Invoice: " + invoice.getInvoiceNumber());
                    List<LineItem> lineItems = DbHelper.getInvoiceLineItems(invoice.getInvoiceNumber());
                    InvoiceHeader invoiceHeader = new InvoiceHeader(
                            invoice.getInvoiceNumber(),
                            invoice.getInvoiceDateTime(),
                            invoice.getSellerTin(),
                            invoice.getBuyerTin(),
                            "",
                            siteId,
                            1,
                            1,
                            1
                    );

                    SalesInvoice invoiceRequest = POSHelper.createInvoiceRequest(
                            invoiceHeader,
                            lineItems,
                            invoice.getInvoiceTotal(),
                            invoice.getTotalVat()
                    );
                    
                    System.out.println("Transmitting invoice " + invoiceRequest.InvoiceHeader.getInvoiceDateTime());

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
                });
                return null;
            }
        };
    }

    public TransmissionService() {
        setPeriod(Duration.minutes(10));
        setDelay(Duration.seconds(5));
        setRestartOnFailure(true);
        setBackoffStrategy(EXPONENTIAL_BACKOFF_STRATEGY);
        setMaximumFailureCount(5);
    }
}
