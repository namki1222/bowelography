package com.rightcode.bowelography.network.Request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest implements Serializable {
    Integer age;
    String hasIll;
    String ill;
    String otherIll;
    String secondill;
    String secondotherIll;
    String concern;
    String otherConcern;
    boolean serviceAgreement;
    boolean privacyAgreement;
}
