package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.notice;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class NoticeContentResponse extends CommonResponse {
    notice data;
}
