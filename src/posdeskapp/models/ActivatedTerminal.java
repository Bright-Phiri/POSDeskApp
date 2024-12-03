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
import java.time.OffsetDateTime;

public class ActivatedTerminal {

    @SerializedName("terminalId")
    private String terminalId;

    @SerializedName("terminalPosition")
    private Integer terminalPosition;  // Using Integer to represent nullable int

    @SerializedName("taxpayerId")
    private Long taxpayerId;

    @SerializedName("activationDate")
    private String activationDate;  // Using OffsetDateTime to match DateTimeOffset

    @SerializedName("terminalCredentials")
    private TerminalCredentials terminalCredentials;

    // Getters and Setters
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Integer getTerminalPosition() {
        return terminalPosition;
    }

    public void setTerminalPosition(Integer terminalPosition) {
        this.terminalPosition = terminalPosition;
    }

    public Long getTaxpayerId() {
        return taxpayerId;
    }

    public void setTaxpayerId(Long taxpayerId) {
        this.taxpayerId = taxpayerId;
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
