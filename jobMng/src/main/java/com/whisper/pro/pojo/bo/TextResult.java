package com.whisper.pro.pojo.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TextResult implements Serializable {

    private Integer id;
    //对应音频文件
    private String audioPath;
    //
    private String language;
    //原始文本
    private String originalText;
    //STT完整文本
    private String text;
    //翻译后的
    private List<LanguageText> languageTextList;

    @Data
    public static class LanguageText implements Serializable{
        //翻译语种
        private String language;
        //Stt 文本片段与翻译
        private List<LanguageSegmentText> segmentList;
    }

    @Data
    public static class LanguageSegmentText implements Serializable{
        private Integer seq;
        //STT 片段文本
        private String text;
        // 对应翻译文本
        private String translateText;
    }
}
