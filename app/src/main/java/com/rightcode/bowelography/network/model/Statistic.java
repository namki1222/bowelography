package com.rightcode.bowelography.network.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic implements Serializable {
    ArrayList<average> average;
    Integer countRecords;
    Statistic_statitic statistic;
    Float percent;
}