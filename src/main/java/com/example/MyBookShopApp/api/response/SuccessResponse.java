package com.example.MyBookShopApp.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {

    private boolean result;
    private String error;

    public SuccessResponse(boolean result) {
        this.result = result;
    }

    public SuccessResponse(String error) {
        this.error = error;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "SuccessResponse{" +
                "result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
