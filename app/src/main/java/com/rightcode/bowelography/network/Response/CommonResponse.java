package com.rightcode.bowelography.network.Response;

import com.rightcode.bowelography.network.model.data;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class CommonResponse {
    public Integer status;
    public Boolean result;
    public String message;
    public String token;

    public CommonResponse() {
    }

    public CommonResponse(Integer status, Boolean result, String message,String token) {
        this.status = status;
        this.result = result;
        this.message = message;
        this.token = token;
    }
}
