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
public class HttpResponseResult {
    private int statusCode;
    private String responseBody;

    // Constructor
    public HttpResponseResult(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    // Getters
    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    // Optional: toString() method for easy printing
    @Override
    public String toString() {
        return "Status Code: " + statusCode + ", Response Body: " + responseBody;
    }
}

