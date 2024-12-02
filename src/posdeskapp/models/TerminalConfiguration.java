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
import java.util.List;

public class TerminalConfiguration {

    private double VersionNo;
    private String TerminalLabel;
    private boolean IsActiveTerminal;
    private String EmailAddress;
    private String PhoneNumber;
    private String TradingName;
    private List<String> AddressLines;
    private TerminalSite TerminalSite;
    private OfflineLimit OfflineLimit;

    // Default constructor
    public TerminalConfiguration() {}

    public double getVersionNo() {
        return VersionNo;
    }

    public void setVersionNo(double versionNo) {
        this.VersionNo = versionNo;
    }

    public String getTerminalLabel() {
        return TerminalLabel;
    }

    public void setTerminalLabel(String terminalLabel) {
        this.TerminalLabel = terminalLabel;
    }

    public boolean getIsActiveTerminal() {
        return IsActiveTerminal;
    }

    public void setIsActiveTerminal(boolean isActiveTerminal) {
        this.IsActiveTerminal = isActiveTerminal;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.EmailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getTradingName() {
        return TradingName;
    }

    public void setTradingName(String tradingName) {
        this.TradingName = tradingName;
    }

    public List<String> getAddressLines() {
        return AddressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.AddressLines = addressLines;
    }

    public TerminalSite getTerminalSite() {
        return TerminalSite;
    }

    public void setTerminalSite(TerminalSite terminalSite) {
        this.TerminalSite = terminalSite;
    }

    public OfflineLimit getOfflineLimit() {
        return OfflineLimit;
    }

    public void setOfflineLimit(OfflineLimit offlineLimit) {
        this.OfflineLimit = offlineLimit;
    }
}

