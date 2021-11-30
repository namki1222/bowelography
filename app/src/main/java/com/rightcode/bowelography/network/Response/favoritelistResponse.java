package com.rightcode.bowelography.network.Response;


import com.rightcode.bowelography.network.model.favorite;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class favoritelistResponse extends CommonResponse {
    ArrayList<favorite> list;
}
