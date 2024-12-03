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
public class POS {

    @Expose
    private String ProductID;
    @Expose
    private String ProductVersion;

    public POS(String productID, String productVersion) {
        this.ProductID = productID;
        this.ProductVersion = productVersion;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String ProductID) {
        this.ProductID = ProductID;
    }

    public String getProductVersion() {
        return ProductVersion;
    }

    public void setProductVersion(String ProductVersion) {
        this.ProductVersion = ProductVersion;
    }
}
