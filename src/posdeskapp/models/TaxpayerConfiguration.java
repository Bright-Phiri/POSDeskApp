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

public class TaxpayerConfiguration {

    @SerializedName("VersionNo")
    private int VersionNo;

    @SerializedName("Tin")
    private String Tin;

    @SerializedName("IsVATRegistered")
    private boolean IsVATRegistered;

    @SerializedName("TaxOfficeCode")
    private String TaxOfficeCode;

    @SerializedName("TaxOffice")
    private TaxOffice TaxOffice;

    @SerializedName("ActivatedTaxRateIds")
    private List<String> ActivatedTaxRateIds;

    @SerializedName("ActivatedTaxrates")
    private Object ActivatedTaxrates;

    // Getters and Setters
    public int getVersionNo() {
        return VersionNo;
    }

    public void setVersionNo(int versionNo) {
        this.VersionNo = versionNo;
    }

    public String getTin() {
        return Tin;
    }

    public void setTin(String tin) {
        this.Tin = tin;
    }

    public boolean isVATRegistered() {
        return IsVATRegistered;
    }

    public void setIsVATRegistered(boolean isVATRegistered) {
        this.IsVATRegistered = isVATRegistered;
    }

    public String getTaxOfficeCode() {
        return TaxOfficeCode;
    }

    public void setTaxOfficeCode(String taxOfficeCode) {
        this.TaxOfficeCode = taxOfficeCode;
    }

    public TaxOffice getTaxOffice() {
        return TaxOffice;
    }

    public void setTaxOffice(TaxOffice taxOffice) {
        this.TaxOffice = taxOffice;
    }

    public List<String> getActivatedTaxRateIds() {
        return ActivatedTaxRateIds;
    }

    public void setActivatedTaxRateIds(List<String> activatedTaxRateIds) {
        this.ActivatedTaxRateIds = activatedTaxRateIds;
    }

    public Object getActivatedTaxrates() {
        return ActivatedTaxrates;
    }

    public void setActivatedTaxrates(Object activatedTaxrates) {
        this.ActivatedTaxrates = activatedTaxrates;
    }
}
