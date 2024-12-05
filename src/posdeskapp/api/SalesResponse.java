/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.api;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.List;
import posdeskapp.models.InvoiceHeader;
import posdeskapp.models.InvoiceLineItem;
import posdeskapp.models.InvoiceSummary;

/**
 *
 * @author biphiri
 */
public class SalesResponse {
    @SerializedName("dateSubmitted")
    private LocalDateTime dateSubmitted;
    @SerializedName("invoiceHeader")
    private InvoiceHeader invoiceHeader;
    @SerializedName("invoiceLineItems")
    private List<InvoiceLineItem> invoiceLineItems;
    @SerializedName("invoiceSummary")
    private InvoiceSummary invoiceSummary;

    // Getters and Setters
    public LocalDateTime getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDateTime dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public InvoiceHeader getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(InvoiceHeader invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public List<InvoiceLineItem> getInvoiceLineItems() {
        return invoiceLineItems;
    }

    public void setInvoiceLineItems(List<InvoiceLineItem> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
    }

    public InvoiceSummary getInvoiceSummary() {
        return invoiceSummary;
    }

    public void setInvoiceSummary(InvoiceSummary invoiceSummary) {
        this.invoiceSummary = invoiceSummary;
    }
}
