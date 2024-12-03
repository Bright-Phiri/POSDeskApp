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
public class TaxRate {

    @SerializedName("Id")
    private String Id;

    @SerializedName("Name")
    private String Name;

    @SerializedName("ChargeMode")
    private String ChargeMode;

    @SerializedName("Ordinal")
    private int Ordinal;

    @SerializedName("Rate")
    private double Rate;

    // Getters and Setters
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getChargeMode() {
        return ChargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.ChargeMode = chargeMode;
    }

    public int getOrdinal() {
        return Ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.Ordinal = ordinal;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        this.Rate = rate;
    }
}
