package com.bz.thirdparty.model.sms;

/**
 * 
 * 作者:唐鹏
 * 创建时间:2017年10月11日下午3:49:51
 * 描述:短信平台返回结果封装
 * 备注:
 */
public class SmsAccountResult extends SmsResult{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033283255953213604L;
	private Result result;//返回信息封装
	
	public Result getResult() {
		return result;
	}


	public void setResult(Result result) {
		this.result = result;
	}



	/**
	 * 
	 * 作者:唐鹏
	 * 创建时间:2017年10月11日下午4:18:52
	 * 描述:内部类封装返回信息
	 * 备注:
	 */
	public class Result {
		 private String account;////返回注册号码
		 
		 private String warnSmsCount;//返回短信余额警告条数
		 
		 private String whiteIps;//返回ip白名单
		 
		 private String balance;//返回余额
		
		 public String getAccount() {
				return account;
			}

			public void setAccount(String account) {
				this.account = account;
			}

			public String getWarnSmsCount() {
				return warnSmsCount;
			}

			public void setWarnSmsCount(String warnSmsCount) {
				this.warnSmsCount = warnSmsCount;
			}

			public String getWhiteIps() {
				return whiteIps;
			}

			public void setWhiteIps(String whiteIps) {
				this.whiteIps = whiteIps;
			}

			public String getBalance() {
				return balance;
			}

			public void setBalance(String balance) {
				this.balance = balance;
			}
		
	}
}
