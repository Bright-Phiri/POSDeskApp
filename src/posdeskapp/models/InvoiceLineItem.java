/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import com.google.gson.annotations.Expose;

/**
 *
 * @author biphiri
 */
public class InvoiceLineItem {

    @Expose
    private int id;
    @Expose
    private String productCode;
    @Expose
    private String description;
    @Expose
    private double unitPrice;
    @Expose
    private double quantity;
    @Expose
    private double discount;
    @Expose
    private double total;
    @Expose
    private double totalVAT;
    @Expose
    private String taxRateId;

    public InvoiceLineItem(int id, String productCode, String description, double unitPrice, double quantity, double discount, double total, double totalVAT, String taxRateId) {
        this.id = id;
        this.productCode = productCode;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
        this.total = total;
        this.totalVAT = totalVAT;
        this.taxRateId = taxRateId;
    }

    public int getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getDescription() {
        return description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }

    public double getTotalVAT() {
        return totalVAT;
    }

    public String getTaxRateId() {
        return taxRateId;
    }
}
