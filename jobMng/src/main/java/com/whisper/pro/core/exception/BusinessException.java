package com.whisper.pro.core.exception;

import com.whisper.pro.core.enums.EnumInterface;
import com.whisper.pro.core.enums.GlobalResultEnum;

/**
 * 
 * @author Administrator
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -6195036395465739764L;
	
	private String exceptionCode;
	
	public BusinessException(EnumInterface errorCodeEnum){
		super(errorCodeEnum.getMessage());
		this.exceptionCode = errorCodeEnum.getCode();
	}
	
	public BusinessException(EnumInterface errorCodeEnum, String msg){
		super(msg);
		this.exceptionCode = errorCodeEnum.getCode();
	}

	public BusinessException(String msg) {
		super(msg);
		this.exceptionCode = GlobalResultEnum.FAILURE.getCode();
	}

	public BusinessException(String exceptionCode, String msg) {
		super(msg);
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * Gets the msg. 如果没有自定义msg，则返回从validatemessages.xml中获取到的msg
	 * 
	 * @return the msg
	 */
	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
