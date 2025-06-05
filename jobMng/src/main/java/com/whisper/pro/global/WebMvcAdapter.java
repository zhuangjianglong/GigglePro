//package com.whisper.pro.global;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TimeZone;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.CacheControl;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import com.zhongrui.common.interceptor.LoginContextInterceptor;
//
//@Configuration
//public class WebMvcAdapter extends WebMvcConfigurerAdapter {
//
//	@Value("${file.upload-path}")
//	private String filePathPrefix;
//	@Value("${login.enable:true}")
//	private boolean logginEnable;
//
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedHeaders("*/*")
//                .allowedMethods("*")
//                .maxAge(120);
//    }
//
//	/**
//     * 拦截器配置
//     * @param registry
//     */
//	@Override
//    public void addInterceptors(InterceptorRegistry registry) {
//		if(logginEnable){
//			//拦截规则：除了excludePathPatterns中地址，其他都拦截判断
//	        registry.addInterceptor(new LoginContextInterceptor())
//	                //添加你不需要拦截的请求路径
//	                .excludePathPatterns("/")
//	                //框架的错误返回请求
//	                .excludePathPatterns("/error/**")
//	                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/doc.html/**")
//	                .excludePathPatterns("/static/**")
//	                //导出文档相关接口
//	                .excludePathPatterns("/export/**")
//	                .excludePathPatterns("/safety/**")
//	                //内部测试相关接口
//	                .excludePathPatterns("/test/**")
//
//	                //添加你要拦截的请求路径，如果有多个路径，就继续addPathPatterns
//	                .addPathPatterns("/**");
//	        super.addInterceptors(registry);
//		}
//    }
//
//	/**
//     * 资源处理器
//     * @param registry
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    	registry.addResourceHandler("/**").addResourceLocations(
//                "classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
//                "classpath:/META-INF/resources/");
//        registry.addResourceHandler("doc.html").addResourceLocations(
//        		"classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations(
//                "classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/static/**").addResourceLocations("file:"+filePathPrefix)
//				.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
//        super.addResourceHandlers(registry);
//    }
//
//    /**
//     * 设置序列化与反序列化
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
//        fastJsonConfig.setSerializerFeatures(
//                SerializerFeature.PrettyFormat,
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteDateUseDateFormat
//            );
//        // 处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//
//        converters.add(fastConverter);
//    }
//
//}