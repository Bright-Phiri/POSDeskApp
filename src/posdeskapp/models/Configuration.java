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

    @SerializedName("globalConfiguration")
    private TaxConfiguration globalConfiguration;

    @SerializedName("terminalConfiguration")
    private TerminalConfiguration terminalConfiguration;

    @SerializedName("taxpayerConfiguration")
    private TaxpayerConfiguration taxpayerConfiguration;

    // Getters and Setters
    public TaxConfiguration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public void setGlobalConfiguration(TaxConfiguration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }

    public TerminalConfiguration getTerminalConfiguration() {
        return terminalConfiguration;
    }

    public void setTerminalConfiguration(TerminalConfiguration terminalConfiguration) {
        this.terminalConfiguration = terminalConfiguration;
    }

    public TaxpayerConfiguration getTaxpayerConfiguration() {
        return taxpayerConfiguration;
    }

    public void setTaxpayerConfiguration(TaxpayerConfiguration taxpayerConfiguration) {
        this.taxpayerConfiguration = taxpayerConfiguration;
    }
}
