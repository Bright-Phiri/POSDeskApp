/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.api;

import com.google.gson.annotations.SerializedName;
import posdeskapp.models.ActivatedTerminal;
import posdeskapp.models.Configuration;

/**
 *
 * @author biphiri
 */
public class TerminalActivationResponse {

    @SerializedName("activatedTerminal")
    private ActivatedTerminal activatedTerminal;

    @SerializedName("configuration")
    private Configuration configuration;

    // Getters and Setters
    public ActivatedTerminal getActivatedTerminal() {
        return activatedTerminal;
    }

    public void setActivatedTerminal(ActivatedTerminal activatedTerminal) {
        this.activatedTerminal = activatedTerminal;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}