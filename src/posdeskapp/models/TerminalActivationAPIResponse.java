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

public class TerminalActivationAPIResponse {

    private int StatusCode;
    private String Remark;
    private TerminalActivationResponse Data;
    private List<APIError> Errors;

    public TerminalActivationAPIResponse() {
        // Default constructor
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public TerminalActivationResponse getData() {
        return Data;
    }

    public void setData(TerminalActivationResponse Data) {
        this.Data = Data;
    }

    public List<APIError> getErrors() {
        return Errors;
    }

    public void setErrors(List<APIError> Errors) {
        this.Errors = Errors;
    }
}

