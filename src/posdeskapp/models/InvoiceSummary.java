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
public class InvoiceSummary {

    @Expose
    private List<TaxBreakDown> taxBreakDown;
    @Expose
    private double totalVAT;
    @Expose
    private String offlineSignature;
    @Expose
    private double invoiceTotal;

    public List<TaxBreakDown> getTaxBreakDown() {
        return taxBreakDown;
    }

    public double getTotalVAT() {
        return totalVAT;
    }

    public String getOfflineSignature() {
        return offlineSignature;
    }

    public double getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setTaxBreakDown(List<TaxBreakDown> taxBreakDown) {
        this.taxBreakDown = taxBreakDown;
    }

    public void setTotalVAT(double totalVAT) {
        this.totalVAT = totalVAT;
    }

    public void setOfflineSignature(String offlineSignature) {
        this.offlineSignature = offlineSignature;
    }

    public void setInvoiceTotal(double invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }
}
