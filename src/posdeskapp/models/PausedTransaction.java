/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import posdeskapp.utils.POSHelper;

/**
 *
 * @author biphiri
 */
public class PausedTransaction {

    private final CheckBox check;
    private final SimpleIntegerProperty pauseId;
    private final SimpleStringProperty transactionDate;
    private final SimpleDoubleProperty total;

    public PausedTransaction(Integer pauseId, String transactionDate, Double total) {
        this.check = new CheckBox();
        this.pauseId = new SimpleIntegerProperty(pauseId);
        this.transactionDate = new SimpleStringProperty(transactionDate);
        this.total = new SimpleDoubleProperty(total);
    }

    public int getPauseId() {
        return pauseId.get();
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }

    public CheckBox getCheck() {
        return check;
    }

    public double getTotal() {
        return total.get();
    }

    public String getFormattedTransactionTotal() {
        return POSHelper.formatValue(getTotal());
    }
}
