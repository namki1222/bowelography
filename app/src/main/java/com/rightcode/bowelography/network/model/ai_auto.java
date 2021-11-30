package com.rightcode.bowelography.network.model;

import android.net.Uri;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ai_auto implements Serializable {
    Uri photo;
    String shape;
    String color;
    int blood;
    String img;
}