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
public class TaxBreakDown {

    private final String rateId;

    private final double taxableAmount;

    private final double taxAmount;

    public TaxBreakDown(String rateId, double taxableAmount, double taxAmount) {
        this.rateId = rateId;
        this.taxableAmount = taxableAmount;
        this.taxAmount = taxAmount;
    }

    public String getRateId() {
        return rateId;
    }

    public double getTaxableAmount() {
        return taxableAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }
}
