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
public class InvoiceHeader {

    @Expose
    private String invoiceNumber;
    @Expose
    private String invoiceDateTime;
    @Expose
    private String sellerTIN;
    @Expose
    private String buyerTIN;
    @Expose
    private String buyerAuthorizationCode;
    @Expose
    private String siteId;
    @Expose
    private int globalConfigVersion;
    @Expose
    private int taxpayerConfigVersion;
    @Expose
    private int terminalConfigVersion;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceDateTime() {
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

    public void setInvoiceDateTime(String invoiceDateTime) {
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
