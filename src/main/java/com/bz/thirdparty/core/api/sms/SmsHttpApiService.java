package com.bz.thirdparty.core.api.sms;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bz.framework.error.exception.ExternalException;
import com.bz.framework.util.http.HttpClientHelper;
import com.bz.thirdparty.model.sms.SmsAccountResult;
import com.bz.thirdparty.model.sms.SmsSendResult;
/**
 * 
 * 作者:唐鹏
 * 创建时间:2017年10月11日下午2:06:26
 * 描述:云通讯短信接口api请求服务
 * 备注:
 */

@Service
public class SmsHttpApiService {

	private static final Log log = LogFactory.getLog(SmsHttpApiService.class);

	/* 账号信息获取地址 */
	@Value("${sms.account}")
	private String Account_Info;
	/*短信验证码发送地址*/
    @Value("${sms.captcha}")
	private String sendCaptcha;
    /*通知短信发送地址*/
    @Value("${sms.notice}")
	private String sendnNotice;
    /*营销短信发送地址*/
    @Value("${sms.marketing}")
	private String sendnMarketing;
    /*apiKEY*/
	@Value("${sms.apikey}")
	private String apiKey;
	
	@Value("${spring.dubbo.application.name}")
	private String name;
	
	private Map<String, String> requstMap;
	
	private HttpClientHelper clientHelper;//htttp请求

	public SmsHttpApiService() {
		clientHelper=new HttpClientHelper(); 
		//requstMap.put("apikey", apiKey);
	}

	/**
	 * 
	 * 作者:唐鹏 创建时间:2017年10月11日下午2:10:21 描述: 判断当前短信平台是否欠费 备注:
	 * 
	 * @return true:是 false:否
	 */
	public boolean isArrears() throws ExternalException {
		SmsAccountResult result=	getCurrySmsAccountInfo();
		log.info("查询短信欠费结果:【"+result+"】");
		return result.getResult().getBalance().equals("0");
	}

	/**
	 * 
	 * 作者:唐鹏 创建时间:2017年10月11日下午2:48:43 描述:获取当前短信平台账号信息 备注:
	 * 
	 * @return
	 */
	public SmsAccountResult getCurrySmsAccountInfo() {
		SmsAccountResult account = new SmsAccountResult();
		requstMap.put("apikey", apiKey);
		clientHelper.addParameter(requstMap);
		try {
			String result = clientHelper.doPost(Account_Info);
			account = getObject(SmsAccountResult.class, result);
		} catch (Exception e) {
			log.error("获取短信账号信息错误", e);
			e.printStackTrace();
		}
		return account;
	}
	
	/**
	 * 
	 * 作者:唐鹏
	 * 创建时间:2017年10月11日下午4:24:29
	 * 描述:验证码短信发送
	 * 备注:
	 * @param telPhone:接收的手机号，仅支持单号码发送
	 * @param content: 短信发送内容（必须经过utf-8格式编码）s
	 * @return
	 */
	public SmsSendResult sendCaptchaSms(String telPhone,String content) {
		SmsSendResult result=new SmsSendResult();
		requstMap.put("apikey", apiKey);
		requstMap.put("mobile", telPhone);
		requstMap.put("content", content);
		clientHelper.addParameter(requstMap);
		try {
			result=getObject(SmsSendResult.class,clientHelper.doPost(sendCaptcha));
		} catch (Exception e) {
			log.error("发送短信验证码失败",e);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * 作者:唐鹏
	 * 创建时间:2017年10月11日下午4:57:30
	 * 描述:营销短信批量发送
	 * 备注:
	 * @param mobile：手机号码（多个号码用英文半角逗号分开，最多可提交1000个）
	 * @param content 短信发送内容（必须经过utf-8格式编码）
	 * @param send_ts 定时发送（必须要以'yyyy-MM-dd HH:mm:ss'形式提交）
	 * @return
	 * @throws ExternalException
	 */
	public SmsSendResult sendMarketingSms(String mobile,String content,String send_ts)throws ExternalException {
		SmsSendResult result=new SmsSendResult();
		requstMap.put("apikey", apiKey);
		requstMap.put("mobile", mobile);
		requstMap.put("content", content);
		if(StringUtils.isNotEmpty(send_ts)) {
			requstMap.put("send_ts", send_ts);
		}
		clientHelper.addParameter(requstMap);
		try {
			result=getObject(SmsSendResult.class,clientHelper.doPost(sendnMarketing));
		} catch (Exception e) {
			log.error("发送短信验证码失败",e);
			e.printStackTrace();
		}
		return result;
			
	}
	
	/**
	 * 
	 * 作者:唐鹏
	 * 创建时间:2017年10月11日下午4:45:01
	 * 描述:发送通知短信
	 * 备注:
	 * @param mobile:电话手机号码（多个号码用英文半角逗号分开）
	 * @param content:内容
	 * @throws ExternalException
	 */
	public SmsSendResult sendtNoticeSms(String mobile,String content)throws ExternalException{
		SmsSendResult result=new SmsSendResult();
		requstMap.put("apikey", apiKey);
		requstMap.put("mobile", mobile);
		requstMap.put("content", content);
		clientHelper.addParameter(requstMap);
		try {
			result=getObject(SmsSendResult.class,clientHelper.doPost(sendnNotice));
		} catch (Exception e) {
			log.error("发送通知短信失败", e);
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 
	 * 作者:唐鹏 创建时间:2017年10月11日下午3:57:38 描述:将消息转换成Java对象 备注:
	 * 
	 * @param t
	 * @param text
	 * @return
	 */
	protected  <T> T getObject(Class<T> classs, String text) {
		JSON json = (JSON) JSON.parse(text);
		T t1 = JSONObject.toJavaObject(json, classs);
		return t1;
	}
}
