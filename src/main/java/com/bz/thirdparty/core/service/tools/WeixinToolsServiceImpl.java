package com.bz.thirdparty.core.service.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.bz.framework.constant.exception.BzExceptionEnum.ExternalExceptionEnum;
import com.bz.framework.error.exception.ExternalException;
import com.bz.open.core.service.weixin.WeixinToolsService;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
/**
 * 
 * 作者:唐鹏
 * 创建时间:2017年10月16日下午4:35:47
 * 描述:微信工具服务实现
 * 备注:
 */
@Service(version="1.0.0",interfaceClass=com.bz.open.core.service.weixin.WeixinToolsService.class)
public class WeixinToolsServiceImpl implements WeixinToolsService{
	private final Logger logger=LoggerFactory.getLogger(WeixinToolsServiceImpl.class);
	
	  @Autowired
	  private WxMpService wxMpService;
	
	@Override
	public void mpOAuth2(String notfUrl) throws ExternalException {
		
		wxMpService.oauth2buildAuthorizationUrl(notfUrl, WxConsts.OAUTH2_SCOPE_USER_INFO, null);
	}

	@Override
	public WxMpOAuth2AccessToken getWxMpOAuth2AccessToken(String code) throws ExternalException {
		 WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
		try {
			wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
		} catch (WxErrorException e) {
			logger.error("获取acc_token失败", e);
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_WEIXIN_TOOLS_ERROR, "获取acc_token失败");
		}
		return wxMpOAuth2AccessToken;
	}

	@Override
	public WxMpUser getWxMpUser(WxMpOAuth2AccessToken token) throws ExternalException {
		try {
		return	wxMpService.oauth2getUserInfo(token, null);
		} catch (WxErrorException e) {
			logger.error("根据token获取用户基本信息失败", e);
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_WEIXIN_TOOLS_ERROR, "根据token获取用户基本信息失败");
		}
	}

	@Override
	public WxMpOAuth2AccessToken refreshAccessToken(WxMpOAuth2AccessToken token) throws ExternalException {
		
		try {
			return wxMpService.oauth2refreshAccessToken(token.getRefreshToken());
		} catch (WxErrorException e) {
			logger.error("刷新acc_token失败", e);
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_WEIXIN_TOOLS_ERROR, "刷新acc_token失败");
		}
	}

	@Override
	public boolean validateAccessToken(WxMpOAuth2AccessToken token) throws ExternalException {
		return wxMpService.oauth2validateAccessToken(token);
	}

	@Override
	public WxMpUser getWxMpUser(String openId, String lang) throws ExternalException {
		try {
			return wxMpService.getUserService().userInfo(openId,lang);
		} catch (WxErrorException e) {
			logger.error("根据token获取用户基本信息失败", e);
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_WEIXIN_TOOLS_ERROR, "根据openId获取用户基本信息失败");
		}
		}

	@Override
	public WxMpUserList getWxMpUserList(String openIds) throws ExternalException {
		try {
			return ((WxMpUserService) wxMpService).userList(openIds);
		} catch (WxErrorException e) {
			logger.error("根据openIds获取部分用户基本信息失败", e);
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_WEIXIN_TOOLS_ERROR, "根据openIds获取部分用户基本信息失败");
		}
	}

}
