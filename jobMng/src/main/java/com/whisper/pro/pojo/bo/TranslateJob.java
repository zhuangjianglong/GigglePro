package com.whisper.pro.pojo.bo;

import lombok.Data;

import java.util.List;

@Data
public class TranslateJob {
	private Integer id;
	//任务类型
	private Integer type;
	//对应sttjob标识
	private Integer sttJobId;
	//对应sttjob文本
	private String sttText;
	//翻译语言
	private String targetLanguage;

	private List<String> targetLanguagelist;
	//任务状态
	private Integer status;
	//重试次数
	private Integer retryCount;
	//翻译结果
	private String resultText;

}
