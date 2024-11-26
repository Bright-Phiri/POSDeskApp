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

public class InvoiceHeader {

    private final String invoiceNumber;
    private final OffsetDateTime invoiceDateTime;
    private final String sellerTIN;
    private final String buyerTIN;
    private final String buyerAuthorizationCode;
    private final String siteId;
    private final int globalConfigVersion;
    private final int taxpayerConfigVersion;
    private final int terminalConfigVersion;

    public InvoiceHeader(String invoiceNumber, OffsetDateTime invoiceDateTime, String sellerTIN, String buyerTIN, String buyerAuthorizationCode, String siteId, int globalConfigVersion, int taxpayerConfigVersion, int terminalConfigVersion) {
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

    public OffsetDateTime getInvoiceDateTime() {
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

