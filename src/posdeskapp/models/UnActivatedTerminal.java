/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import com.google.gson.annotations.Expose;

/**
 *
 * @author biphiri
 */
public class UnActivatedTerminal {

    @Expose
    private String TerminalActivationCode;
    @Expose
    private TerminalRuntimeEnvironment Environment;

    public UnActivatedTerminal(String terminalActivationCode, TerminalRuntimeEnvironment environment) {
        this.TerminalActivationCode = terminalActivationCode;
        this.Environment = environment;
    }

    public String getTerminalActivationCode() {
        return TerminalActivationCode;
    }

    public void setTerminalActivationCode(String TerminalActivationCode) {
        this.TerminalActivationCode = TerminalActivationCode;
    }

    public TerminalRuntimeEnvironment getEnvironment() {
        return Environment;
    }

    public void setEnvironment(TerminalRuntimeEnvironment Environment) {
        this.Environment = Environment;
    }
}
