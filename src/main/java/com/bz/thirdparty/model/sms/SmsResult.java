package com.bz.thirdparty.model.sms;

import com.bz.framework.pojo.base.BasePojo;
/**
 * 
 * 作者:唐鹏
 * 创建时间:2017年10月11日下午2:47:21
 * 描述:云通讯短信平台账号信息封装
 * 备注:
 */
public abstract class SmsResult extends BasePojo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5815838972539098981L;
	
	
	 private String code;///返回结果，code为1，表示成功，其他请参考返回值说明
	 
	 private String msg;//msg
	 
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
}
