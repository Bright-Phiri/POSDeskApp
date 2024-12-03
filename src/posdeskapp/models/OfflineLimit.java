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

    @SerializedName("maxTransactionAgeInHours")
    private int maxTransactionAgeInHours;

    @SerializedName("maxCummulativeAmount")
    private int maxCummulativeAmount;

    // Getters and Setters
    public int getMaxTransactionAgeInHours() {
        return maxTransactionAgeInHours;
    }

    public void setMaxTransactionAgeInHours(int maxTransactionAgeInHours) {
        this.maxTransactionAgeInHours = maxTransactionAgeInHours;
    }

    public double getMaxCummulativeAmount() {
        return maxCummulativeAmount;
    }

    public void setMaxCummulativeAmount(int maxCummulativeAmount) {
        this.maxCummulativeAmount = maxCummulativeAmount;
    }
}
