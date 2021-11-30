package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic_statitic implements Serializable {
    Statistic_data shape;
    Statistic_data_1 color;
    Statistic_data_2 mass;
    Statistic_data_3 hematochezia;
    Statistic_data_4 colic;
    Statistic_data_5 smell;
    Statistic_data_6 appearanceEtc;
    Statistic_data_7 etc;

}