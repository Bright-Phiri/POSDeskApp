/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import posdeskapp.utils.POSHelper;

/**
 *
 * @author biphiri
 */
public class LineItem {
    private SimpleIntegerProperty id;
    private SimpleStringProperty productCode;
    private SimpleStringProperty description;
    private SimpleDoubleProperty unitPrice;
    private SimpleDoubleProperty quantity;
    private SimpleStringProperty invoiceNumber;
    private SimpleStringProperty taxRateId;
    private SimpleDoubleProperty discount;
    private SimpleDoubleProperty total;
    private SimpleDoubleProperty totalVAT;
    private HBox controlsPane;

    public LineItem() {
    }

    public LineItem(String productCode, String description, double quantity, double unitPrice, double total, double discount, double totalVAT, String taxRateId, HBox controlsPane) {
        this.id = new SimpleIntegerProperty(0);
        this.productCode = new SimpleStringProperty(productCode);
        this.description = new SimpleStringProperty(description);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.quantity = new SimpleDoubleProperty(1.0f);
        this.invoiceNumber = new SimpleStringProperty("");
        this.taxRateId = new SimpleStringProperty(taxRateId);
        this.discount = new SimpleDoubleProperty(discount);
        this.total = new SimpleDoubleProperty(total);
        this.totalVAT = new SimpleDoubleProperty(totalVAT);
        this.controlsPane = controlsPane;
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getProductCode() {
        return productCode.get();
    }

    public String getDescription() {
        return description.get();
    }

    public Double getUnitPrice() {
        return unitPrice.get();
    }

    public Double getQuantity() {
        return quantity.get();
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public String getTaxRateId() {
        return taxRateId.get();
    }

    public Double getDiscount() {
        return discount.get();
    }

    public Double getTotal() {
        return total.get();
    }

    public Double getTotalVAT() {
        return totalVAT.get();
    }

    public HBox getControlsPane() {
        return controlsPane;
    }

    public String getFormattedUnitPrice() {
        return POSHelper.formatValue(getUnitPrice());
    }

    public String getFormattedTotal() {
        return POSHelper.formatValue(getTotal());
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setProductCode(String productCode) {
        this.productCode = new SimpleStringProperty(productCode);
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
    }

    public void setQuantity(Double quantity) {
        this.quantity = new SimpleDoubleProperty(quantity);
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
    }

    public void setTaxRateId(String taxRateId) {
        this.taxRateId = new SimpleStringProperty(taxRateId);
    }

    public void setDiscount(Double discount) {
        this.discount =  new SimpleDoubleProperty(discount);
    }

    public void setTotal(Double total) {
        this.total = new SimpleDoubleProperty(total);
    }

    public void setTotalVAT(Double totalVAT) {
        this.totalVAT = new SimpleDoubleProperty(totalVAT);
    }

    public void setControlsPane(HBox controlsPane) {
        this.controlsPane = controlsPane;
    }   
}