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

    @SerializedName("versionNo")
    private int versionNo;

    @SerializedName("tin")
    private String tin;

    @SerializedName("isVATRegistered")
    private boolean isVATRegistered;

    @SerializedName("taxOfficeCode")
    private String taxOfficeCode;

    @SerializedName("taxOffice")
    private TaxOffice taxOffice;

    @SerializedName("activatedTaxRateIds")
    private List<String> activatedTaxRateIds;

    @SerializedName("activatedTaxrates")
    private Object activatedTaxrates;

    public int getVersionNo() {
        return versionNo;
    }

    public String getTin() {
        return tin;
    }

    public boolean isIsVATRegistered() {
        return isVATRegistered;
    }

    public String getTaxOfficeCode() {
        return taxOfficeCode;
    }

    public TaxOffice getTaxOffice() {
        return taxOffice;
    }

    public List<String> getActivatedTaxRateIds() {
        return activatedTaxRateIds;
    }

    public Object getActivatedTaxrates() {
        return activatedTaxrates;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public void setIsVATRegistered(boolean isVATRegistered) {
        this.isVATRegistered = isVATRegistered;
    }

    public void setTaxOfficeCode(String taxOfficeCode) {
        this.taxOfficeCode = taxOfficeCode;
    }

    public void setTaxOffice(TaxOffice taxOffice) {
        this.taxOffice = taxOffice;
    }

    public void setActivatedTaxRateIds(List<String> activatedTaxRateIds) {
        this.activatedTaxRateIds = activatedTaxRateIds;
    }

    public void setActivatedTaxrates(Object activatedTaxrates) {
        this.activatedTaxrates = activatedTaxrates;
    }
}
