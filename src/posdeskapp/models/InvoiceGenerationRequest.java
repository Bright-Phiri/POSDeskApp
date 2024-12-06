/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import java.time.LocalDateTime;

/**
 *
 * @author biphiri
 */
public class InvoiceGenerationRequest {

    private long businessId;
    private int terminalPosition;
    private int transactionCount;
    private LocalDateTime transactionDate; // Using LocalDateTime for date and time
    private int numItems;
    private double vatAmount;
    private double invoiceTotal;
    
    public InvoiceGenerationRequest(long businessId, int terminalPosition, int transactionCount, LocalDateTime transactionDate, int numItems, double vatAmount, double invoiceTotal) {
        this.businessId = businessId;
        this.terminalPosition = terminalPosition;
        this.transactionCount = transactionCount;
        this.transactionDate = transactionDate;
        this.numItems = numItems;
        this.vatAmount = vatAmount;
        this.invoiceTotal = invoiceTotal;
    }

    // Getters and Setters
    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public int getTerminalPosition() {
        return terminalPosition;
    }

    public void setTerminalPosition(int terminalPosition) {
        this.terminalPosition = terminalPosition;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public double getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(double invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }
}
