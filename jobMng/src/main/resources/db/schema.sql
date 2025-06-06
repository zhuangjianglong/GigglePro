DROP TABLE IF EXISTS tb_stt_job;
CREATE TABLE tb_stt_job(
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    audio_id INT COMMENT 'audio主键ID',
    audio_path varchar(1024) not null COMMENT 'audio文件路径，冗余',
    original_text_path varchar(1024) COMMENT 'audio文件对应原始文本文件路径，冗余',
    type TINYINT not null comment '1-whisper;2-',
    status TINYINT comment '0-成功；1-新建；2-执行中；4-取消；3-whisper异常；9-未知错误',
    retry_count TINYINT,
    text_path varchar(1024)
);
ALTER TABLE tb_stt_job ALTER COLUMN id RESTART WITH 1000;

DROP TABLE IF EXISTS tb_translate_job;
CREATE TABLE tb_translate_job(
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    stt_job_id INT not null,
    type TINYINT not null comment '1-google;2-baidu;3-dl',
    target_language char(8) not null,
    status TINYINT comment '0-成功；1-新建；2-执行中；3-whisper异常；9-未知错误',
    retry_count TINYINT,
    result_path varchar(1024)
);
ALTER TABLE tb_translate_job ALTER COLUMN id RESTART WITH 1000;
