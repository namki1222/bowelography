package com.rightcode.bowelography.network;


import com.rightcode.bowelography.network.Response.CommonResponse;

public interface ApiResponseHandler<T extends CommonResponse> {
    void onSuccess(T result);
    void onServerFail(CommonResponse result);
    void onNoResponse(T result);
    void onBadRequest(Throwable t);
}
