package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.calendar_condtion;
import com.rightcode.bowelography.network.model.calendardate;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class CalendarCondtionResponse extends CommonResponse {
    calendar_condtion data;
}
