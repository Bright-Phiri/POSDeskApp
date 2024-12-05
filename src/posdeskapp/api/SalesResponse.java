/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.api;

import java.util.List;
import posdeskapp.models.InvoiceHeader;
import posdeskapp.models.InvoiceLineItem;
import posdeskapp.models.InvoiceSummary;

/**
 *
 * @author biphiri
 */
public class SalesResponse {
    private String dateSubmitted;
    private InvoiceHeader invoiceHeader;
    private List<InvoiceLineItem> invoiceLineItems;
    private InvoiceSummary invoiceSummary;

    // Getters and Setters
    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
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
