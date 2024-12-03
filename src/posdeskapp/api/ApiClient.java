/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import posdeskapp.utils.DbHelper;

/**
 *
 * @author biphiri
 */
public class ApiClient {

    public static HttpResponseResult pingServer(String bearerToken) {
        String jsonBody = "";
        return sendHttpPostRequest(ApiConfig.PING_SERVER, jsonBody, false, "");
    }

    public static HttpResponseResult submitSalesTransaction(String invoicePayload) {
        return sendHttpPostRequest(ApiConfig.SUBMIT_SALES_TRANSACTION, invoicePayload, true, "");
    }

    public static HttpResponseResult activateTerminal(String unActivteTerminalPayload) {
        return sendHttpPostRequest(ApiConfig.ACTIVATE_TERMINAL, unActivteTerminalPayload, false, "");
    }

    public static HttpResponseResult confirmTerminalActivation(String activteTerminalPayload, String xSignature) {
        return sendHttpPostRequest(ApiConfig.CONFIRM_TERMINAL_ACTIVATION, activteTerminalPayload, false, xSignature);
    }

    public static HttpResponseResult sendHttpPostRequest(String uri, String jsonBody, boolean includeAuthorization, String xSignature) {
        int statusCode = -1;
        String responseBody = "";
        String token = includeAuthorization ? DbHelper.getTerminalJwtToken() : null;

        try {
            // Trust all certificates
            TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            try (CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build()) {
                URI requestUri = new URI(uri);

                HttpPost postRequest = new HttpPost(requestUri);
                postRequest.setHeader("Content-Type", "application/json");
                if (includeAuthorization && token != null) {
                    postRequest.setHeader("Authorization", "Bearer " + token);
                }
                if (xSignature != null && !xSignature.isEmpty()) {
                    postRequest.setHeader("X-Signature", xSignature);
                }
                postRequest.setEntity(new StringEntity(jsonBody));

                HttpResponse response = client.execute(postRequest);

                statusCode = response.getStatusLine().getStatusCode();
                responseBody = EntityUtils.toString(response.getEntity());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new HttpResponseResult(statusCode, responseBody);
    }

}
