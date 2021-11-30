package com.rightcode.bowelography.network.Response;

import lombok.Getter;

@Getter
public class Common_1Response {
    public Integer status;
    public Boolean result_1;
    public String message;
    public String token;

    public Common_1Response() {
    }

    public Common_1Response(Integer status, Boolean result, String message, String token) {
        this.status = status;
        this.result_1 = result;
        this.message = message;
        this.token = token;
    }
}
