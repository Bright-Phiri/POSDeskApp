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

    public InvoiceHeader(String invoiceNumber, String invoiceDateTime, String sellerTIN, String buyerTIN, String buyerAuthorizationCode, String siteId, int globalConfigVersion, int taxpayerConfigVersion, int terminalConfigVersion) {
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
}
