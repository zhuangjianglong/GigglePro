package com.whisper.pro.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_translate_job")
public class TranslateJobPO {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("type")
	private Integer type;
	@TableField("target_language")
	private String targetLanguage;
	@TableField("status")
	private Integer status;
	@TableField("retry_count")
	private Integer retryCount;
	@TableField("stt_job_id")
	private Integer sttJobId;
	@TableField("result_path")
	private String resultPath;

}
