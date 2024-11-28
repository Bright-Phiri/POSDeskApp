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
import java.time.LocalDateTime;

public class InvoiceHeader {

    private  String invoiceNumber;
    private  LocalDateTime invoiceDateTime;
    private  String sellerTIN;
    private  String buyerTIN;
    private  String buyerAuthorizationCode;
    private  String siteId;
    private  int globalConfigVersion;
    private  int taxpayerConfigVersion;
    private  int terminalConfigVersion;

    public InvoiceHeader() {
    }

    
    public InvoiceHeader(String invoiceNumber, LocalDateTime invoiceDateTime, String sellerTIN, String buyerTIN, String buyerAuthorizationCode, String siteId, int globalConfigVersion, int taxpayerConfigVersion, int terminalConfigVersion) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDateTime = invoiceDateTime;
        this.sellerTIN = sellerTIN;
        this.buyerTIN = buyerTIN;
        this.buyerAuthorizationCode = buyerAuthorizationCode;
        this.siteId = siteId;
        this.globalConfigVersion = globalConfigVersion;
        this.taxpayerConfigVersion = taxpayerConfigVersion;
        this.terminalConfigVersion = terminalConfigVersion;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDateTime getInvoiceDateTime() {
        return invoiceDateTime;
    }

    public String getSellerTIN() {
        return sellerTIN;
    }

    public String getBuyerTIN() {
        return buyerTIN;
    }

    public String getBuyerAuthorizationCode() {
        return buyerAuthorizationCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public int getGlobalConfigVersion() {
        return globalConfigVersion;
    }

    public int getTaxpayerConfigVersion() {
        return taxpayerConfigVersion;
    }

    public int getTerminalConfigVersion() {
        return terminalConfigVersion;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
}

    public void setInvoiceDateTime(LocalDateTime invoiceDateTime) {
        this.invoiceDateTime = invoiceDateTime;
    }

    public void setSellerTIN(String sellerTIN) {
        this.sellerTIN = sellerTIN;
    }

    public void setBuyerTIN(String buyerTIN) {
        this.buyerTIN = buyerTIN;
    }

    public void setBuyerAuthorizationCode(String buyerAuthorizationCode) {
        this.buyerAuthorizationCode = buyerAuthorizationCode;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setGlobalConfigVersion(int globalConfigVersion) {
        this.globalConfigVersion = globalConfigVersion;
    }

    public void setTaxpayerConfigVersion(int taxpayerConfigVersion) {
        this.taxpayerConfigVersion = taxpayerConfigVersion;
    }

    public void setTerminalConfigVersion(int terminalConfigVersion) {
        this.terminalConfigVersion = terminalConfigVersion;
    }
}

