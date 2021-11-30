package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class notice implements Serializable {
    Integer id;
    String createdAt;
    String subject;
    String content;

}