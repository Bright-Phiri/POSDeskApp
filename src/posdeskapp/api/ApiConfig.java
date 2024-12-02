/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.api;

/**
 *
 * @author biphiri
 */
// src/com/yourapp/api/ApiConfig.java

public class ApiConfig {
    public static final String BASE_URL = "https://localhost:7162/api/v1";

    // sales endpoints
    public static final String SUBMIT_SALES_TRANSACTION = BASE_URL + "/sales/submit-sales-transaction";
    public static final String GET_INVOICE_STATUS = BASE_URL + "/sales/get-invoice-status";
}

