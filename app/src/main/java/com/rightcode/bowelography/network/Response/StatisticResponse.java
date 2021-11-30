package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.Statistic;
import com.rightcode.bowelography.network.model.advertisement;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class StatisticResponse extends CommonResponse {
    Statistic data;
}
