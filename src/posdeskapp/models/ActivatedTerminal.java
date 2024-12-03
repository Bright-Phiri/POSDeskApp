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

public class ActivatedTerminal {

    @SerializedName("terminalId")
    private String terminalId;

    @SerializedName("activationDate")
    private String activationDate;

    @SerializedName("terminalCredentials")
    private TerminalCredentials terminalCredentials;

    // Getters and Setters
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public TerminalCredentials getTerminalCredentials() {
        return terminalCredentials;
    }

    public void setTerminalCredentials(TerminalCredentials terminalCredentials) {
        this.terminalCredentials = terminalCredentials;
    }
}
