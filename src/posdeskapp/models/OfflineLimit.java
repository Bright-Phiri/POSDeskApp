/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author biphiri
 */
public class OfflineLimit {

    private double maxTransactionAgeInHours;
    @SerializedName("MaxTransactionAgeInHours")
    private int MaxTransactionAgeInHours;

    @SerializedName("MaxCummulativeAmount")
    private double MaxCummulativeAmount;

    // Getters and Setters
    public int getMaxTransactionAgeInHours() {
        return MaxTransactionAgeInHours;
    }

    public void setMaxTransactionAgeInHours(int maxTransactionAgeInHours) {
        this.MaxTransactionAgeInHours = maxTransactionAgeInHours;
    }

    public double getMaxCummulativeAmount() {
        return MaxCummulativeAmount;
    }

    public void setMaxCummulativeAmount(double maxCummulativeAmount) {
        this.MaxCummulativeAmount = maxCummulativeAmount;
    }
}
