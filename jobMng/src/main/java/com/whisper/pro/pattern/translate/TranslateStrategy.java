package com.whisper.pro.pattern.translate;

import com.whisper.pro.pojo.bo.TranslateItemResult;

import java.util.List;

public interface TranslateStrategy {

    List<TranslateItemResult> translate(List<String> textList, String targetLanguage);

}
