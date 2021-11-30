package com.rightcode.bowelography.network.Response;



import android.text.Html;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class ai_photoResponse extends CommonResponse {
    String shape;
    String color;
    Float blood;
    String img;
}
