package posdeskapp.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse<T> {

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("remark")
    private String remark;

    @SerializedName("data")
    private T data;

    @SerializedName("errors")
    private List<Object> errors;

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }
}
