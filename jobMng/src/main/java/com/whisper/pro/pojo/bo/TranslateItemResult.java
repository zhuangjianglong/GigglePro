package com.whisper.pro.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TranslateItemResult implements Serializable {
    private Integer seq;
    private final String text;
    private final String sourceLanguage;
    private final String model;
}
