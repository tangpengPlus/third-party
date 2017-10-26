package com.bz.thirdparty.model.sms;

/**
 * 
 * 作者:唐鹏
 * 创建时间:2017年10月11日下午4:21:30
 * 描述:验证码发送消息返回封装
 * 备注:
 */
public class SmsSendResult extends SmsResult{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String result; //此次短信验证码发送的批次号，可用于查询短信发送状态

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	

}
