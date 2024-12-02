/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 *
 * @author biphiri
 */
public class SalesInvoice {

    @Expose
    public InvoiceHeader InvoiceHeader;
    @Expose
    public List<InvoiceLineItem> InvoiceLineItems;
    @Expose
    public InvoiceSummary InvoiceSummary;
}
