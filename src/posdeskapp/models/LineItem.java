/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author biphiri
 */
public class LineItem {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty productCode;
    private final SimpleStringProperty description;
    private final SimpleFloatProperty unitPrice;
    private final SimpleFloatProperty quantity;
    private final SimpleStringProperty invoiceNumber;
    private final SimpleStringProperty taxRateId;
    private final SimpleFloatProperty discount;
    
    public LineItem(int id, String productCode, String  description, Float unitPrice,Float quantity, String  invoiceNumber, String taxRateId, Float discount){
      this.id = new SimpleIntegerProperty(id);
      this.productCode = new SimpleStringProperty(productCode);
      this.description = new SimpleStringProperty(description);
      this.unitPrice = new SimpleFloatProperty(unitPrice);
      this.quantity = new SimpleFloatProperty(quantity);
      this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
      this.taxRateId = new SimpleStringProperty(taxRateId);
      this.discount = new SimpleFloatProperty(discount);
    }

    public int getId() {
        return id.get();
    }

    public String getProductCode() {
        return productCode.get();
    }

    public String getDescription() {
        return description.get();
    }

    public Float getUnitPrice() {
        return unitPrice.get();
    }

    public Float getQuantity() {
        return quantity.get();
    }

    public String getInvoiceNumber() {
        return invoiceNumber.get();
    }

    public String getTaxRateId() {
        return taxRateId.get();
    }

    public Float getDiscount() {
        return discount.get();
    }
}
