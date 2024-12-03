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
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TaxConfiguration {

    @SerializedName("Id")
    private int Id;

    @SerializedName("VersionNo")
    private int VersionNo;

    @SerializedName("Taxrates")
    private List<TaxRate> TaxRates;

    // Getters and Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getVersionNo() {
        return VersionNo;
    }

    public void setVersionNo(int versionNo) {
        this.VersionNo = versionNo;
    }

    public List<TaxRate> getTaxRates() {
        return TaxRates;
    }

    public void setTaxRates(List<TaxRate> taxRates) {
        this.TaxRates = taxRates;
    }
}
