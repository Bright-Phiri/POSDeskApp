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
public class TerminalActivationResponse {

    private ActivatedTerminal ActivatedTerminal;
    private Configuration Configuration;

    public TerminalActivationResponse() {
        // Default constructor
    }

    public ActivatedTerminal getActivatedTerminal() {
        return ActivatedTerminal;
    }

    public void setActivatedTerminal(ActivatedTerminal ActivatedTerminal) {
        this.ActivatedTerminal = ActivatedTerminal;
    }

    public Configuration getConfiguration() {
        return Configuration;
    }

    public void setConfiguration(Configuration Configuration) {
        this.Configuration = Configuration;
    }
}

