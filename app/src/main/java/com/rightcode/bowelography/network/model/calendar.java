package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class calendar implements Serializable {
    Integer year;
    Integer month;
    Integer date;

}