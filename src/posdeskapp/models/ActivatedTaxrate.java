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
public class ActivatedTaxrate{

    private long Id;
    private long TaxpayerConfigurationId;
    private String TaxRateId;

    public ActivatedTaxrate() {}

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public long getTaxpayerConfigurationId() {
        return TaxpayerConfigurationId;
    }

    public void setTaxpayerConfigurationId(long taxpayerConfigurationId) {
        this.TaxpayerConfigurationId = taxpayerConfigurationId;
    }

    public String getTaxRateId() {
        return TaxRateId;
    }

    public void setTaxRateId(String taxRateId) {
        this.TaxRateId = taxRateId;
    }
}

