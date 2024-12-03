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

    @SerializedName("versionNo")
    private int versionNo;

    @SerializedName("terminalLabel")
    private String terminalLabel;

    @SerializedName("emailAddress")
    private String emailAddress;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("tradingName")
    private String tradingName;

    @SerializedName("addressLines")
    private List<String> addressLines;

    @SerializedName("offlineLimit")
    private OfflineLimit offlineLimit;

    // Getters and Setters
    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getTerminalLabel() {
        return terminalLabel;
    }

    public void setTerminalLabel(String terminalLabel) {
        this.terminalLabel = terminalLabel;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }

    public OfflineLimit getOfflineLimit() {
        return offlineLimit;
    }

    public void setOfflineLimit(OfflineLimit offlineLimit) {
        this.offlineLimit = offlineLimit;
    }
}
