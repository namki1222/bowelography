package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.advertisement;
import com.rightcode.bowelography.network.model.calendar_condtion;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class AdvertisementResponse extends CommonResponse {
    ArrayList<advertisement> list;
}
