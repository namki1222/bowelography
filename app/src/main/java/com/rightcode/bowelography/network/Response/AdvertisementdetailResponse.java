package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.advertisement;
import com.rightcode.bowelography.network.model.advertisement_detail;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class AdvertisementdetailResponse extends CommonResponse {
    advertisement_detail data;
}
