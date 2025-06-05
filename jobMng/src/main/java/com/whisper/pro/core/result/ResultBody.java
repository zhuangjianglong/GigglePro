package com.whisper.pro.core.result;

import com.whisper.pro.core.enums.EnumInterface;
import com.whisper.pro.core.enums.GlobalResultEnum;

import java.io.Serializable;

public class ResultBody<T> implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4685910902965274218L;

    private String code;

    private String msg;

    private T data;
    
    public ResultBody(){}

    public ResultBody(EnumInterface resultInfo) {
        this.code = resultInfo.getCode();
        this.msg = resultInfo.getMessage();
    }
    
    public ResultBody(EnumInterface resultInfo, String msg) {
        this.code = resultInfo.getCode();
        this.msg = msg;
    }

    public ResultBody(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public ResultBody(T data) {
    	this.code = GlobalResultEnum.SUCCESS.getCode();
        this.msg = GlobalResultEnum.SUCCESS.getMessage();
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}