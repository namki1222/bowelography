package com.rightcode.bowelography.network.Request;



import com.rightcode.bowelography.network.Response.CommonResponse;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtraReportRequest_not_sure implements Serializable {
    boolean notSure;
}
