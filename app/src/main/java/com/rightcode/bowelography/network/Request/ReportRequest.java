package com.rightcode.bowelography.network.Request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest implements Serializable {
    String time;
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
    boolean isChanged;
}
