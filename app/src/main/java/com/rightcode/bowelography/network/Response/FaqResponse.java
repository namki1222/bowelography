package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.FAQ;
import com.rightcode.bowelography.network.model.calendardate;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class FaqResponse extends CommonResponse {
    ArrayList<FAQ> list;
}
