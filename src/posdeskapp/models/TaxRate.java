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
public class TaxRate {

    private String Id;
    private String Name;
    private String ChargeMode;
    private int Ordinal;
    private double Rate;

    public TaxRate() {}

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

