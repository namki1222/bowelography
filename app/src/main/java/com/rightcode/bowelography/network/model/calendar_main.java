package com.rightcode.bowelography.network.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class calendar_main implements Serializable {
    ArrayList<calendar> normal;
    ArrayList<calendar_ai> ai;

}