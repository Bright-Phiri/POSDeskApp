/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import java.util.List;

/**
 *
 * @author biphiri
 */
public class SalesInvoice {
    public InvoiceHeader invoiceHeader;
    public List<LineItem> invoiceLineItems;
    public InvoiceSummary invoiceSummary;

    public SalesInvoice(InvoiceHeader invoiceHeader, List<LineItem> invoiceLineItems, InvoiceSummary invoiceSummary) {
        this.invoiceHeader = invoiceHeader;
        this.invoiceLineItems = invoiceLineItems;
        this.invoiceSummary = invoiceSummary;
    }
}
