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
    public static final String PING_SERVER = "utilities/ping";
    public static final String ACTIVATE_TERMINAL = "onboarding/activate-terminal";
    public static final String CONFIRM_TERMINAL_ACTIVATION = "onboarding/terminal-activated-confirmation";
}
