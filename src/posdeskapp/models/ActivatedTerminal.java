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

    @SerializedName("TerminalId")
    private String TerminalId;

    @SerializedName("ActivationDate")
    private String ActivationDate;

    @SerializedName("TerminalCredentials")
    private TerminalCredentials TerminalCredentials;

    // Getters and Setters
    public String getTerminalId() {
        return TerminalId;
    }

    public void setTerminalId(String terminalId) {
        this.TerminalId = terminalId;
    }

    public String getActivationDate() {
        return ActivationDate;
    }

    public void setActivationDate(String activationDate) {
        this.ActivationDate = activationDate;
    }

    public TerminalCredentials getTerminalCredentials() {
        return TerminalCredentials;
    }

    public void setTerminalCredentials(TerminalCredentials terminalCredentials) {
        this.TerminalCredentials = terminalCredentials;
    }
}
