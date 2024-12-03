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
public class Configuration {

    @SerializedName("TaxConfiguration")
    private TaxConfiguration GlobalConfiguration;

    @SerializedName("TerminalConfiguration")
    private TerminalConfiguration TerminalConfiguration;

    @SerializedName("TaxpayerConfiguration")
    private TaxpayerConfiguration TaxpayerConfiguration;

    // Getters and Setters
    public TaxConfiguration getGlobalConfiguration() {
        return GlobalConfiguration;
    }

    public void setTaxConfiguration(TaxConfiguration GlobalConfiguration) {
        this.GlobalConfiguration = GlobalConfiguration;
    }

    public TerminalConfiguration getTerminalConfiguration() {
        return TerminalConfiguration;
    }

    public void setTerminalConfiguration(TerminalConfiguration terminalConfiguration) {
        this.TerminalConfiguration = terminalConfiguration;
    }

    public TaxpayerConfiguration getTaxpayerConfiguration() {
        return TaxpayerConfiguration;
    }

    public void setTaxpayerConfiguration(TaxpayerConfiguration taxpayerConfiguration) {
        this.TaxpayerConfiguration = taxpayerConfiguration;
    }
}
