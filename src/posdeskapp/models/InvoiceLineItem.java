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

    public void setId(int id) {
        this.id = id;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotalVAT(double totalVAT) {
        this.totalVAT = totalVAT;
    }

    public void setTaxRateId(String taxRateId) {
        this.taxRateId = taxRateId;
    }

}
