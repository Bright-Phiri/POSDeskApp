/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author biphiri
 */
public class Product {

    private final SimpleStringProperty productCode;
    private final SimpleStringProperty description;
    private final SimpleDoubleProperty unitPrice;
    private final SimpleStringProperty unitOfMeasure;
    private final SimpleDoubleProperty quantity;
    private final SimpleStringProperty expireDate;
    private final SimpleStringProperty taxRateId;

    public Product(String productCode, String description, double unitPrice, String unitOfMeasure, double quantity, String expireDate, String taxRateId) {
        this.productCode = new SimpleStringProperty(productCode);
        this.description = new SimpleStringProperty(description);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.unitOfMeasure = new SimpleStringProperty(unitOfMeasure);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.expireDate = new SimpleStringProperty(expireDate);
        this.taxRateId = new SimpleStringProperty(taxRateId);
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

    public String getUnitOfMeasure() {
        return unitOfMeasure.get();
    }

    public Double getQuantity() {
        return quantity.get();
    }

    public String getExpireDate() {
        return expireDate.get();
    }

    public String getTaxRateId() {
        return taxRateId.get();
    }
}
