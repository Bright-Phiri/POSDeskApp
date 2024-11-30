/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import java.util.List;

/**
 *
 * @author biphiri
 */
public class InvoiceSummary {
    public List<TaxBreakDown> taxBreakDown;
    public double totalVAT;
    public String offlineSignature;
    public double invoiceTotal;
}
