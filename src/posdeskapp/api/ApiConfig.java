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
public class ApiConfig {

    public static final String BASE_URL = "https://dev-eis-api.mra.mw/api/v1/";

    //End-points
    public static final String SUBMIT_SALES_TRANSACTION = BASE_URL + "sales/submit-sales-transaction";
    public static final String GET_INVOICE_STATUS = BASE_URL + "sales/get-invoice-status";
    public static final String PING_SERVER = BASE_URL + "utilities/ping";
    public static final String ACTIVATE_TERMINAL = BASE_URL+  "onboarding/activate-terminal";
    public static final String CONFIRM_TERMINAL_ACTIVATION = BASE_URL + "onboarding/terminal-activated-confirmation";
    public static final String GET_TERMINAL_SITE_PRODUCTS =  BASE_URL + "utilities/get-terminal-site-products";
    public static final String GET_LAST_SUBMITTED_ONLINE_TRANSACTION =  BASE_URL + "sales/last-submitted-online-transaction";
    public static final String GET_LAST_SUBMITTED_OFFLINE_TRANSACTION =  BASE_URL + "sales/last-submitted-offline-transaction";
}
