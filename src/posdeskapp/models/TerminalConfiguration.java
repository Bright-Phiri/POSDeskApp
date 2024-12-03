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
import java.util.List;

public class TerminalConfiguration {

    @SerializedName("VersionNo")
    private int VersionNo;

    @SerializedName("TerminalLabel")
    private String TerminalLabel;

    @SerializedName("EmailAddress")
    private String EmailAddress;

    @SerializedName("PhoneNumber")
    private String PhoneNumber;

    @SerializedName("TradingName")
    private String TradingName;

    @SerializedName("AddressLines")
    private List<String> AddressLines;

    @SerializedName("OfflineLimit")
    private OfflineLimit OfflineLimit;

    // Getters and Setters
    public int getVersionNo() {
        return VersionNo;
    }

    public void setVersionNo(int versionNo) {
        this.VersionNo = versionNo;
    }

    public String getTerminalLabel() {
        return TerminalLabel;
    }

    public void setTerminalLabel(String terminalLabel) {
        this.TerminalLabel = terminalLabel;
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

    public OfflineLimit getOfflineLimit() {
        return OfflineLimit;
    }

    public void setOfflineLimit(OfflineLimit offlineLimit) {
        this.OfflineLimit = offlineLimit;
    }
}
