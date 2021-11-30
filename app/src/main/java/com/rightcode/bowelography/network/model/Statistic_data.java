package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic_data implements Serializable {
    Integer 딱딱;
    Integer 단단;
    Integer 건조;
    Integer 매끈;
    Integer 물렁;
    Integer 찰흙;
    Integer 물;

}