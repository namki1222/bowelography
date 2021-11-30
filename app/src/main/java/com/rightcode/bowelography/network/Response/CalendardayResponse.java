package com.rightcode.bowelography.network.Response;



import com.rightcode.bowelography.network.model.calendar;
import com.rightcode.bowelography.network.model.calendardate;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class CalendardayResponse extends CommonResponse {
    ArrayList<calendardate> list;
}
