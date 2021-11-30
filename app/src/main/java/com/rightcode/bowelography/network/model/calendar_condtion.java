package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class calendar_condtion implements Serializable {
    String condition;
    Integer weight;
}