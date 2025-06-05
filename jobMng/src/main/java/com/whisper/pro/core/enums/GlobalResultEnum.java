package com.whisper.pro.core.enums;

/**
 * 全局接口异常枚举定义
 *
 * @author Administrator
 */
public enum GlobalResultEnum implements EnumInterface {

    SUCCESS("0000", "Success"),

    FAILURE("9999", "Failure"),

    W_JOB_NOFOUND("6000", "stt job not found."),
    W_JOB_CANCLE("6001", "stt job status error."),
    CALL_WHISPER_ERROR("6010", "whisper call error."),


    T_NOFOUND_S("8000", "未找到对应配置的翻译策略实现"),
    T_G_ERROR("8010", "谷歌翻译错误"),

    DEMO("xxxxx", "xxxxxxxxx");


    GlobalResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
