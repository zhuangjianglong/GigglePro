package com.whisper.pro.configuration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


/**
 *
 * 数据库配置类
 * @author
 *
 */
@Configuration
@MapperScan(basePackages = {"com.whisper.pro.dao"})
public class MyBatisConfiguration {

}