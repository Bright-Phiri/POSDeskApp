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
public class TaxOffice {

    @SerializedName("TaxOfficeCode")
    private String TaxOfficeCode;

    @SerializedName("TaxOfficeName")
    private String TaxOfficeName;

    // Getters and Setters
    public String getTaxOfficeCode() {
        return TaxOfficeCode;
    }

    public void setTaxOfficeCode(String taxOfficeCode) {
        this.TaxOfficeCode = taxOfficeCode;
    }

    public String getTaxOfficeName() {
        return TaxOfficeName;
    }

    public void setTaxOfficeName(String taxOfficeName) {
        this.TaxOfficeName = taxOfficeName;
    }
}
