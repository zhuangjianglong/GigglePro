package com.whisper.pro.core.enums;
/**
 * 主数据类型配置枚举表
 * @author Administrator
 *
 */
public enum MainDataEnum implements EnumInterface{

	MDT_COMM("common","基础域下存放各平台相关地址信息"),
	
	MDT_ITP("itp","一体化教学平台相关配置"),
	MDT_OMP("omp","运营管理平台相关配置"),
	MDT_TRMP("trmp","教学资源管理平台相关配置"),
	MDT_UUMS("uums","统一用户管理系统相关配置"),
	MDT_DAP("dap","数据分析平台相关配置"),
	MDT_QCP("qcp","资质认证平台相关配置"),
	
	MDT_RACK("rack","机架平台相关配置"),
	MDT_STACK("stack","虚拟化平台相关配置"),
	MDT_LOG("log","日志平台相关配置"),
	MDT_CONFIG("config","元数据配置平台相关配置"),
	MDT_UPGRADE("upgrade","升级模块相关配置");
	
	private String code;
	private String message;
	
	MainDataEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
	
	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
