package posdeskapp.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import posdeskapp.utils.POSHelper;

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
        this.id = new SimpleIntegerProperty(0);
        this.productCode = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.unitPrice = new SimpleDoubleProperty(0.0);
        this.quantity = new SimpleDoubleProperty(0.0);
        this.invoiceNumber = new SimpleStringProperty("");
        this.taxRateId = new SimpleStringProperty("");
        this.discount = new SimpleDoubleProperty(0.0);
        this.total = new SimpleDoubleProperty(0.0);
        this.totalVAT = new SimpleDoubleProperty(0.0);
        this.controlsPane = null; // Can be set later
    }

    public LineItem(String productCode, String description, double quantity, double unitPrice, double discount, double totalVAT, String taxRateId, String invoiceNumber) {
        this();
        this.productCode.set(productCode);
        this.description.set(description);
        this.unitPrice.set(unitPrice);
        this.quantity.set(quantity);
        this.discount.set(discount);
        this.totalVAT.set(totalVAT);
        this.taxRateId.set(taxRateId);
        this.invoiceNumber.set(invoiceNumber);
        calculateTotal();
    }

    public LineItem(String productCode, String description, double quantity, double unitPrice, double total, double discount, double totalVAT, String taxRateId, HBox controlsPane) {
        this(productCode, description, quantity, unitPrice, discount, totalVAT, taxRateId, "");
        this.controlsPane = controlsPane;
        this.total.set(total);
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
        this.productCode.set(productCode);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice.set(unitPrice);
        calculateTotal();
    }

    public void setQuantity(Double quantity) {
        this.quantity.set(quantity);
        calculateTotal();
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber.set(invoiceNumber);
    }

    public void setTaxRateId(String taxRateId) {
        this.taxRateId.set(taxRateId);
    }

    public void setDiscount(Double discount) {
        this.discount.set(discount);
        calculateTotal();
    }

    public void setTotal(Double total) {
        this.total.set(total);
    }

    public void setTotalVAT(Double totalVAT) {
        this.totalVAT.set(totalVAT);
    }

    public void setControlsPane(HBox controlsPane) {
        this.controlsPane = controlsPane;
    }

    public void calculateTotal() {
        double calculatedTotal = (this.quantity.get() * this.unitPrice.get()) - this.discount.get();
        this.total.set(calculatedTotal);
    }
}
