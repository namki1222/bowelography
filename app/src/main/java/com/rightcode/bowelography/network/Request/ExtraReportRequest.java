package com.rightcode.bowelography.network.Request;

import com.rightcode.bowelography.network.model.favorite;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtraReportRequest implements Serializable {
    Integer recordId;
    String category;
    String contents;
}
