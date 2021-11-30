package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAQ implements Serializable {
    Integer id;
    String category;
    Integer sequence;
    String subject;
    String content;

}