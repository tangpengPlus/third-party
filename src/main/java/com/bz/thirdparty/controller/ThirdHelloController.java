package com.bz.thirdparty.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ThirdHelloController {
	/* 账号信息获取地址 */
	@Value("${sms.account}")
	private String Account_Info;
	/**
	 * 
	 * @return
	 */
	@GetMapping(value="/index")
	public @ResponseBody String index(){
		System.out.println(Account_Info);
		return "hello i am is bz_third-party server";
	}
}
