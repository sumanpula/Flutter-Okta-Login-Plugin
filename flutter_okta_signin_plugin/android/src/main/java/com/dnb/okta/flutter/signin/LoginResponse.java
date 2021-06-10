package com.dnb.okta.flutter.signin;

import java.io.Serializable;

/**
 * Created by Suman Pula on 6/9/2021.
 * Copy Right @ Prabhakar Reddy Gudipati.
 * EMAIL : suman07.india@gmail.com.
 */
public class LoginResponse implements Serializable {
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    String error;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    String statusCode;
}
