/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.models;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author biphiri
 */
public class TerminalCredentials {

    @SerializedName("JwtToken")
    private String JwtToken;

    @SerializedName("SecretKey")
    private String SecretKey;

    // Getters and Setters
    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.JwtToken = jwtToken;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        this.SecretKey = secretKey;
    }
}
