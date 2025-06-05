package com.whisper.pro.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_stt_job")
public class SttJobPO {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("audio_id")
	private Integer audioId;
	@TableField("audio_path")
	private String audioPath;
	@TableField("original_text_path")
	private String originalTextPath;
	@TableField("type")
	private Integer type;
	@TableField("status")
	private Integer status;
	@TableField("retry_count")
	private Integer retryCount;
	@TableField("text_path")
	private String textPath;

}
