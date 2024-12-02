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

public class TaxConfiguration {

    private long Id;
    private double VersionNo;
    private List<TaxRate> Taxrates;

    public TaxConfiguration() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public double getVersionNo() {
        return VersionNo;
    }

    public void setVersionNo(double VersionNo) {
        this.VersionNo = VersionNo;
    }

    public List<TaxRate> getTaxrates() {
        return Taxrates;
    }

    public void setTaxrates(List<TaxRate> Taxrates) {
        this.Taxrates = Taxrates;
    }
}

