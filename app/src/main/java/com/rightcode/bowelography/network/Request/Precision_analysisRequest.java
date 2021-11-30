package com.rightcode.bowelography.network.Request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Precision_analysisRequest implements Serializable {
    String email;
    String phoneNumber;
    String residence;

}
