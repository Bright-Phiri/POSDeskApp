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
import java.time.OffsetDateTime;

public class ActivatedTerminal {

    private String TerminalId;
    private Integer TerminalPosition;
    private long TaxpayerId;
    private OffsetDateTime ActivationDate;
    private TerminalCredentials TerminalCredentials;

    public ActivatedTerminal() {
        // Default constructor
    }

    public String getTerminalId() {
        return TerminalId;
    }

    public void setTerminalId(String TerminalId) {
        this.TerminalId = TerminalId;
    }

    public Integer getTerminalPosition() {
        return TerminalPosition;
    }

    public void setTerminalPosition(Integer TerminalPosition) {
        this.TerminalPosition = TerminalPosition;
    }

    public long getTaxpayerId() {
        return TaxpayerId;
    }

    public void setTaxpayerId(long TaxpayerId) {
        this.TaxpayerId = TaxpayerId;
    }

    public OffsetDateTime getActivationDate() {
        return ActivationDate;
    }

    public void setActivationDate(OffsetDateTime ActivationDate) {
        this.ActivationDate = ActivationDate;
    }

    public TerminalCredentials getTerminalCredentials() {
        return TerminalCredentials;
    }

    public void setTerminalCredentials(TerminalCredentials TerminalCredentials) {
        this.TerminalCredentials = TerminalCredentials;
    }
}
