/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import posdeskapp.utils.POSHelper;

/**
 *
 * @author biphiri
 */
public class Invoice {

    private final SimpleStringProperty invoiceNumber;
    private final SimpleStringProperty invoiceDateTime;
    private final SimpleDoubleProperty invoiceTotal;
    private final SimpleStringProperty buyerTin;
    private final SimpleDoubleProperty totalVat;

    public Invoice(String invoiceNumber, String invoiceDateTime, Double invoiceTotal, String buyerTin, Double totalVat) {
        this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
        this.invoiceDateTime = new SimpleStringProperty(invoiceDateTime);
        this.invoiceTotal = new SimpleDoubleProperty(invoiceTotal);
        this.buyerTin = new SimpleStringProperty(buyerTin);
        this.totalVat = new SimpleDoubleProperty(totalVat);
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public String getInvoiceDateTime() {
        return invoiceDateTime.get();
    }

    public double getInvoiceTotal() {
        return invoiceTotal.get();
    }

    public String getBuyerTin() {
        return buyerTin.get();
    }

    public double getTotalVat() {
        return totalVat.get();
    }

    public String getFormattedInvoiceTotal() {
        return POSHelper.formatValue(getInvoiceTotal());
    }

    public String getFormattedInvoiceVAT() {
        return POSHelper.formatValue(getTotalVat());
    }
}
