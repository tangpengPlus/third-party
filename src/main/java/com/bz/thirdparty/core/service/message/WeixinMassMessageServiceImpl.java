package com.bz.thirdparty.core.service.message;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.bz.dao.mapper.message.MessageRecordMapper;
import com.bz.dao.pojo.message.MessageRecord;
import com.bz.framework.constant.exception.BzExceptionEnum.ExternalExceptionEnum;
import com.bz.framework.error.exception.ExternalException;
import com.bz.framework.util.base.ListUtil;
import com.bz.open.core.service.weixin.WeixinMessageService;
import com.bz.open.core.vo.weixin.WeixinMassMessage;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.WxMpMassVideo;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
/**
 * 
 * 作者:唐鹏
 * 创建时间:2017年10月16日上午11:53:45
 * 描述:微信群发消息实现
 * 备注:纯文本消息，图片消息，图文消息、语音消息
 */
@Service(version="1.0.0",interfaceClass=com.bz.open.core.service.weixin.WeixinMessageService.class)
@Transactional
public class WeixinMassMessageServiceImpl implements WeixinMessageService{
  private final Logger logger=LoggerFactory.getLogger(WeixinMassMessageServiceImpl.class);
  @Autowired
  private WxMpService wxMpService;
  @Autowired
  private MessageRecordMapper messageRecordMapper;
	@Override
	public boolean sendTextMessage(WeixinMassMessage message) throws ExternalException {
		WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
		logger.info("发送消息【文本消息】");
		massMessage.setMsgType(WxConsts.MASS_MSG_TEXT);
		if(null==message) {
			logger.error("发送微信群发消息【文本格式】错误原因:{1:发送消息实体【WeixinMessage】对象为空}");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】对象为空");
		}
		if(StringUtils.isEmpty(message.getCount())) {
			logger.error("发送微信群发消息【文本格式】错误原因:{1:发送消息实体【WeixinMessage】属性->count为空}");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【count】为空");
		}
		//设置文本消息内容
		massMessage.setContent(message.getCount());
		//设置消息发送对象
		if(message.isSendToOne()){
			if(StringUtils.isEmpty(message.getWeixinUserOpenId())) {
				logger.error("发送微信群发消息【文本格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为true时属性【WeixinUserOpenId】为空}");
				throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为true时属性【WeixinUserOpenId】为空");
			}
			massMessage.getToUsers().add(message.getWeixinUserOpenId());
		}else {
			if(ListUtil.isEmpty(message.getWeixinUserOpenIds())){
				logger.error("发送微信群发消息【文本格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为false时属性【WeixinUserOpenIds】为空}");
			     throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为false时属性【WeixinUserOpenIds】为空");
			}else {
				for(String opendid:message.getWeixinUserOpenIds()) {
					massMessage.getToUsers().add(opendid);
				}
			}
			WxMpMassSendResult massResult=null;
			try {
				massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
				logger.info("消息发送返回结果:"+massResult);
				if(StringUtils.isEmpty(massResult.getErrorMsg())) {
					return false;
				}
				addSendMessageRecord(message);
			} catch (WxErrorException e) {
				logger.error("发送微信群发消息【文本消息错误】请求微信服务器报错,错误原因:【"+massResult.getErrorMsg()+"】",e);
				throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR,massResult.getErrorMsg());
			}
		}
		return false;
	}
	
	
	@Override
	public boolean sendVideoMessage(WeixinMassMessage message) throws ExternalException {
	   logger.info("发送视频文件消息");
	   if(null==message) {
		    logger.error("发送微信群发消息【视频格式】错误原因:{1:发送消息实体【WeixinMessage】对象为空}");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】对象为空");
	   }
	   if(StringUtils.isEmpty(message.getTilte())) {
		    logger.error("发送微信群发消息【视频格式】错误原因:{1:发送消息实体【WeixinMessage】属性【title】为空}");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【title】为空");
	   }
	   if(StringUtils.isEmpty(message.getCount())) {
		    logger.error("发送微信群发消息【视频格式】错误原因:{1:发送消息实体【WeixinMessage】属性【count】为空}");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【count】为空");
	   }
	   if(null==message.getInputStream()) {
		   logger.error("发送微信群发消息【视频格式】错误原因:{1:发送消息实体【WeixinMessage】属性【inputstream】为空}");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【inputstream】为空");
	   }
	   //组装视频文件
	   WxMediaUploadResult uploadMediaRes=null;
	   try {
		 uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MEDIA_VIDEO, "mp4", message.getInputStream());
	} catch (WxErrorException e) {
		logger.error("组装视频文件到微信服务器失败", e);
		throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "组装视频文件到微信服务器失败请检查属性【inputstream】");
	}

	// 把视频变成可被群发的媒体
	   WxMpMassVideo video = new WxMpMassVideo();
	   video.setTitle(message.getTilte());
	   video.setDescription(message.getCount());
	   video.setMediaId(uploadMediaRes.getMediaId());
	   WxMpMassUploadResult uploadResult;
	try {
		uploadResult = wxMpService.getMassMessageService().massVideoUpload(video);
	} catch (WxErrorException e) {
		logger.error("转换视频格式错误", e);
		throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "把视频变成可被群发的媒体错误");
	}

	   WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
	   massMessage.setMsgType(WxConsts.MASS_MSG_VIDEO);
	   massMessage.setMediaId(uploadResult.getMediaId());
	   //组装接收人
	   if(message.isSendToOne()){
			if(StringUtils.isEmpty(message.getWeixinUserOpenId())) {
				logger.error("发送微信群发消息【视频格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为true时属性【WeixinUserOpenId】为空}");
				throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为true时属性【WeixinUserOpenId】为空");
			}
			massMessage.getToUsers().add(message.getWeixinUserOpenId());
		}else {
			if(ListUtil.isEmpty(message.getWeixinUserOpenIds())){
				logger.error("发送微信群发消息【视频格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为false时属性【WeixinUserOpenIds】为空}");
			     throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为false时属性【WeixinUserOpenIds】为空");
			}else {
				for(String opendid:message.getWeixinUserOpenIds()) {
					massMessage.getToUsers().add(opendid);
				}
			}
			WxMpMassSendResult massResult=null;
			try {
				massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
				logger.info("消息发送返回结果:"+massResult);
				if(StringUtils.isEmpty(massResult.getErrorMsg())) {
					return false;
				}
				addSendMessageRecord(message);
			} catch (WxErrorException e) {
				logger.error("发送微信群发消息【视频消息错误】请求微信服务器报错,错误原因:【"+massResult.getErrorMsg()+"】",e);
				throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR,massResult.getErrorMsg());
			}
		}
	   
		return false;
	}


	@Override
	public boolean sendImageMessage(WeixinMassMessage message) throws ExternalException {
		logger.info("发送消息【图片消息】");
		 if(null==message.getInputStream()) {
			   logger.error("发送微信群发消息【图片格式】错误原因:{1:发送消息实体【WeixinMessage】属性【inputstream】为空}");
				throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【inputstream】为空");
		   }
		 WxMediaUploadResult uploadMediaRes = null;
		try {
			uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MEDIA_IMAGE, "jpg",message.getInputStream());
		} catch (WxErrorException e1) {
			logger.error("发送图片消息时上传图片文件到微信服务器失败", e1);
			e1.printStackTrace();
		}
		 WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
		 massMessage.setMsgType(WxConsts.MASS_MSG_IMAGE);
		 massMessage.setMediaId(uploadMediaRes.getMediaId());
		  //组装接收人
		   if(message.isSendToOne()){
				if(StringUtils.isEmpty(message.getWeixinUserOpenId())) {
					logger.error("发送微信群发消息【图片格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为true时属性【WeixinUserOpenId】为空}");
					throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为true时属性【WeixinUserOpenId】为空");
				}
				massMessage.getToUsers().add(message.getWeixinUserOpenId());
			}else {
				if(ListUtil.isEmpty(message.getWeixinUserOpenIds())){
					logger.error("发送微信群发消息【图片格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为false时属性【WeixinUserOpenIds】为空}");
				     throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为false时属性【WeixinUserOpenIds】为空");
				}else {
					for(String opendid:message.getWeixinUserOpenIds()) {
						massMessage.getToUsers().add(opendid);
					}
				}
				WxMpMassSendResult massResult=null;
				try {
					massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
					logger.info("发送图片消息发送返回结果:"+massResult);
					if(StringUtils.isEmpty(massResult.getErrorMsg())) {
						return false;
					}
					addSendMessageRecord(message);
				} catch (WxErrorException e) {
					logger.error("发送微信群发消息【图片消息错误】请求微信服务器报错,错误原因:【"+massResult.getErrorMsg()+"】",e);
					throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR,massResult.getErrorMsg());
				}
			}
		return false;
	}


	@Override
	public boolean sendVoiceMessage(WeixinMassMessage message) throws ExternalException {
		logger.info("发送消息【语音消息】");
		 if(null==message.getInputStream()) {
			   logger.error("发送微信群发消息【mp3格式】错误原因:{1:发送消息实体【WeixinMessage】属性【inputstream】为空}");
				throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【inputstream】为空");
		   }
		 WxMediaUploadResult uploadMediaRes = null;
		try {
			uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MEDIA_VOICE, "mp3",message.getInputStream());
		} catch (WxErrorException e1) {
			logger.error("发送语音消息时上传文件到微信服务器失败", e1);
			e1.printStackTrace();
		}
		 WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
		 massMessage.setMsgType(WxConsts.MASS_MSG_VOICE);
		 massMessage.setMediaId(uploadMediaRes.getMediaId());
		  //组装接收人
		   if(message.isSendToOne()){
				if(StringUtils.isEmpty(message.getWeixinUserOpenId())) {
					logger.error("发送微信群发消息【MP3格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为true时属性【WeixinUserOpenId】为空}");
					throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为true时属性【WeixinUserOpenId】为空");
				}
				massMessage.getToUsers().add(message.getWeixinUserOpenId());
			}else {
				if(ListUtil.isEmpty(message.getWeixinUserOpenIds())){
					logger.error("发送微信群发消息【MP3格式】错误原因:{1:发送消息实体【WeixinMessage】属性->isSendToOne为false时属性【WeixinUserOpenIds】为空}");
				     throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR, "发送消息实体【WeixinMessage】属性【isSendToOne】为false时属性【WeixinUserOpenIds】为空");
				}else {
					for(String opendid:message.getWeixinUserOpenIds()) {
						massMessage.getToUsers().add(opendid);
					}
				}
				WxMpMassSendResult massResult=null;
				try {
					massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
					logger.info("发送语音消息发送返回结果:"+massResult);
					if(StringUtils.isEmpty(massResult.getErrorMsg())) {
						return false;
					}
					addSendMessageRecord(message);
				} catch (WxErrorException e) {
					logger.error("发送微信群发消息【语音消息错误】请求微信服务器报错,错误原因:【"+massResult.getErrorMsg()+"】",e);
					throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_WEIXI_MASS_NMESSAGE_ERROR,massResult.getErrorMsg());
				}
			}
		return false;
	}
	/**
	 * 
	 * 作者:唐鹏
	 * 创建时间:2017年10月16日下午3:59:20
	 * 描述:将发送微信消息记录存入数据库中
	 * 备注:
	 * @param message:消息实体封装{@link WeixinMassMessage}
	 * @throws WxErrorException
	 */
	private void addSendMessageRecord(WeixinMassMessage message)throws WxErrorException{
		if(message.isRecord()) {
			MessageRecord messageRecord=new MessageRecord();
			messageRecord.setCount(message.getCount());
			messageRecord.setTile(message.getTilte());
			messageRecord.setType(message.getMsgType().getTitle());
			messageRecord.setCreatetime(new Date());
			messageRecord.setFileids(message.getFileIds());
			messageRecordMapper.save(messageRecord);
		}
		
	}
}
