package com.whisper.pro.pojo.bo;


import lombok.Data;

import java.io.Serializable;

@Data
public class SttJob implements Serializable {

    private Integer id;
    //对应音频文件标识
    private Integer audioId;
    //对应音频文件地址，冗余设置
    private String audioPath;
    //对应音频原始文本信息
    private String originalText;
    //STT任务类型   1-whisper
    private Integer type;
    //任务状态
    private Integer status;
    //重试次数
    private Integer retryCount;
    //STT文本
    private String resultText;



}
