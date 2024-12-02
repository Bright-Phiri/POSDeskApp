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
import java.util.List;

public class TaxpayerConfiguration {

    private double VersionNo;
    private String Tin;
    private boolean IsVATRegistered;
    private String TaxOfficeCode;
    private TaxOffice TaxOffice;
    private List<String> ActivatedTaxRateIds;
    private List<ActivatedTaxrate> ActivatedTaxrates;

    public TaxpayerConfiguration() {}

    public double getVersionNo() {
        return VersionNo;
    }

    public void setVersionNo(double versionNo) {
        this.VersionNo = versionNo;
    }

    public String getTin() {
        return Tin;
    }

    public void setTin(String tin) {
        this.Tin = tin;
    }

    public boolean isIsVATRegistered() {
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

    public List<ActivatedTaxrate> getActivatedTaxrates() {
        return ActivatedTaxrates;
    }

    public void setActivatedTaxrates(List<ActivatedTaxrate> activatedTaxrates) {
        this.ActivatedTaxrates = activatedTaxrates;
    }
}

