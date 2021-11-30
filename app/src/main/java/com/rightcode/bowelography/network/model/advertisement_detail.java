package com.rightcode.bowelography.network.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class advertisement_detail implements Serializable {
    Integer id;
    String category;
    Integer sequence;
    String advertiseName;
    String startDate;
    String endDate;
    String url;
    String thumbnail;
    String detailImage;

}