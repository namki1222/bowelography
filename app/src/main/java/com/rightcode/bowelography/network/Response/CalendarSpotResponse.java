package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.calendar;
import com.rightcode.bowelography.network.model.calendar_main;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class CalendarSpotResponse extends CommonResponse {
    calendar_main list;
}
