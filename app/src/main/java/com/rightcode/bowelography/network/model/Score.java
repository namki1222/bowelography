package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Score implements Serializable {
    Integer score;
    String content;
}