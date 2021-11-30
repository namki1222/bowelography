package com.rightcode.bowelography.network.Request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class favoriteRequest implements Serializable {
    String category;
    String contents;
}
