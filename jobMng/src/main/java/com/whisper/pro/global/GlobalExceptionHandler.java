package com.whisper.pro.global;

import javax.servlet.http.HttpServletRequest;

import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import com.whisper.pro.core.result.ResultBody;
import com.whisper.pro.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	protected static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = BusinessException.class)
    public ResultBody<String> handlerBusinessException(HttpServletRequest request,
                                                       BusinessException exception) {
		logger.error(ExceptionUtils.getErrorStack(exception));
        return new ResultBody<String>(exception.getExceptionCode(), exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResultBody<String> handlerException(HttpServletRequest request,
                                           Exception exception) {
    	logger.error(ExceptionUtils.getErrorStack(exception));
        return new ResultBody<String>(GlobalResultEnum.FAILURE);
    }
}
