package com.whisper.pro.global;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.result.ResultBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@SuppressWarnings("rawtypes")
@ControllerAdvice(annotations = RestController.class)
public class ApiResultHandle implements ResponseBodyAdvice{

	//private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    private static final Class[] annos = {
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            PutMapping.class
    };

    /**
     * 对所有RestController的接口方法进行拦截
     */
    @SuppressWarnings("unchecked")
	@Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        AnnotatedElement element = returnType.getAnnotatedElement();
        if(returnType.getMethod().getReturnType().equals(ResponseEntity.class)){
        	return Boolean.FALSE;
        }
        return Arrays.stream(annos).anyMatch(anno -> anno.isAnnotation() && element.isAnnotationPresent(anno));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, 
    		Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    	Object out = null;
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if(body == null){
        	out = new ResultBody(GlobalResultEnum.SUCCESS);
        	
        }else if(body instanceof ResultBody){
            out = body;
            
        }else if (body instanceof GlobalResultEnum){
        	GlobalResultEnum resultCode = (GlobalResultEnum) body;
            out = new ResultBody(resultCode);
            
        }else{
        	out = new ResultBody<Object>(body);
        }
        return out;
    }


}
