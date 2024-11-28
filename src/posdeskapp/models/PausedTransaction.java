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
public class PausedTransaction {

    private final SimpleIntegerProperty pauseId;
    private final SimpleStringProperty transactionDate;
    private final SimpleDoubleProperty total;
    private final HBox controlsPane;

    public PausedTransaction(Integer pauseId, String transactionDate, Double total, HBox controlsPane) {
        this.pauseId = new SimpleIntegerProperty(pauseId);
        this.transactionDate = new SimpleStringProperty(transactionDate);
        this.total = new SimpleDoubleProperty(total);
        this.controlsPane = controlsPane;
    }

    public int getPauseId() {
        return pauseId.get();
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }

    public double getTotal() {
        return total.get();
    }

    public String getFormattedTransactionTotal() {
        return POSHelper.formatValue(getTotal());
    }

    public HBox getControlsPane() {
        return controlsPane;
    }

}
