package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class advertisement implements Serializable {
    Integer id;
    String category;
    Integer sequence;
    String advertiseName;
    String thumbnail;
    String detailImage;
    String url;

}