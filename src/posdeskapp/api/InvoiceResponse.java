/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.api;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author biphiri
 */
public class InvoiceResponse {

    @SerializedName("validationURL")
    private String validationURL;

    @SerializedName("validationErrors")
    private Object validationErrors;

    // Getters and Setters
    public String getValidationURL() {
        return validationURL;
    }

    public void setValidationURL(String validationURL) {
        this.validationURL = validationURL;
    }

    public Object getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Object validationErrors) {
        this.validationErrors = validationErrors;
    }
}
