package com.rightcode.bowelography.network.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class calendardate implements Serializable {
    Integer id;
    String time;
    String thumbnail;
    Integer score;
    String shape;
    String color;
    String mass;
    String hematochezia;
    String hematocheziaPosition;
    String appearanceEtc;
    String colic;
    String smell;
    String elapsedTime;
    String etc;
    ArrayList<calendar_additional> additionalRecords;
}