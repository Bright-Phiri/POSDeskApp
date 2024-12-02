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
public class TerminalCredentials {

    private String JwtToken;
    private String SecretKey;

    public TerminalCredentials() {
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String JwtToken) {
        this.JwtToken = JwtToken;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String SecretKey) {
        this.SecretKey = SecretKey;
    }
}

