/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

/**
 *
 * @author biphiri
 */
public class OfflineLimit {

    private double maxTransactionAgeInHours;
    private double maxCummulativeAmount;

    // Default constructor
    public OfflineLimit() {}

    public double getMaxTransactionAgeInHours() {
        return maxTransactionAgeInHours;
    }

    public void setMaxTransactionAgeInHours(double maxTransactionAgeInHours) {
        this.maxTransactionAgeInHours = maxTransactionAgeInHours;
    }

    public double getMaxCummulativeAmount() {
        return maxCummulativeAmount;
    }

    public void setMaxCummulativeAmount(double maxCummulativeAmount) {
        this.maxCummulativeAmount = maxCummulativeAmount;
    }
}

