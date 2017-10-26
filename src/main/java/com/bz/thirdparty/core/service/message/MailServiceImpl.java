package com.bz.thirdparty.core.service.message;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;
import com.bz.framework.constant.exception.BzExceptionEnum.ExternalExceptionEnum;
import com.bz.framework.error.exception.ExternalException;
import com.bz.open.core.service.mail.MailService;
import com.bz.open.core.vo.mail.MailVo;
/**
 * 
 * 作者:唐鹏
 * 创建时间:2017年10月17日下午2:58:11
 * 描述:邮件发送实现
 * 备注:
 */
@Service(version="1.0.0",interfaceClass=com.bz.open.core.service.mail.MailService.class)
public class MailServiceImpl implements MailService{
    private final Logger logger=LoggerFactory.getLogger(MailServiceImpl.class);
	
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String Sender;
	
	@Override
	public boolean sendEmail(MailVo mailVo) throws ExternalException {
		logger.info("发送邮件信息:【"+mailVo+"】");
		Assert.isNull(mailVo);
		if(StringUtils.isEmpty(mailVo.getTilte())){
			logger.error("发送邮件信息失败参数对象【mailVo】中title对象为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_MAIL_ERROR, "发送邮件信息失败参数对象【mailVo】中title对象为空");
		}
		if(StringUtils.isEmpty(mailVo.getCount())) {
			logger.error("发送邮件信息失败参数对象【mailVo】中count对象为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_MAIL_ERROR, "发送邮件信息失败参数对象【mailVo】中count对象为空");
		}
		if(StringUtils.isEmpty(mailVo.getTheRecipients())) {
			logger.error("发送邮件信息失败参数对象【mailVo】中theRecipients对象为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_MAIL_ERROR, "发送邮件信息失败参数对象【mailVo】中theRecipients对象为空");
		}
		if(mailVo.getMailType()==null) {
			logger.error("发送邮件信息失败【mailType】属性为空，请选择邮件类型 1简单文本邮件 2:Html邮件 3:带附件的邮件 4:带静态资源的邮件");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_MAIL_ERROR, "发送邮件信息失败【mailType】属性为空，请选择邮件类型 1简单文本邮件 2:Html邮件 3:带附件的邮件 ");
		}
		if(mailVo.getMailType().intValue()!=1||mailVo.getMailType().intValue()!=2||mailVo.getMailType().intValue()!=3) {
			logger.error("发送邮件信息失败【mailType】属性值错误，请选择正确的邮件类型 1简单文本邮件 2:Html邮件 3:带附件的邮件 4:带静态资源的邮件");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_SEND_MAIL_ERROR, "发送邮件信息失败【mailType】属性值错误，请选择邮件类型 1简单文本邮件 2:Html邮件 3:带附件的邮件 ");
		}
		//普通文本邮件
		if(mailVo.getMailType().intValue()==1) {
			    SimpleMailMessage message = new SimpleMailMessage();
		        message.setFrom(Sender);
		        message.setTo(mailVo.getTheRecipients()); 
		        message.setSubject("主题："+mailVo.getTilte());
		        message.setText(mailVo.getCount());
		        mailSender.send(message);
		}else if(mailVo.getMailType().intValue()==2) {
			MimeMessage message = null;
	        try {
	            message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(Sender);
	            helper.setTo(mailVo.getTheRecipients());
	            helper.setSubject("标题："+mailVo.getTilte());
	            helper.setText(mailVo.getCount(), true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        mailSender.send(message);
		}else if(mailVo.getMailType().intValue()==3) {
			 MimeMessage message = null;
		        try {
		            message = mailSender.createMimeMessage();
		            MimeMessageHelper helper = new MimeMessageHelper(message, true);
		            helper.setFrom(Sender);
		            helper.setTo(mailVo.getTheRecipients());
		            helper.setSubject("标题："+mailVo.getTilte());
		            helper.setText(mailVo.getCount());
		            //注意项目路径问题，自动补用项目路径
		            FileSystemResource file = new FileSystemResource(mailVo.getFile());
		            //加入邮件
		            helper.addAttachment("附件", file);
		        } catch (Exception e){
		            e.printStackTrace();
		        }
		        mailSender.send(message);
			
		}
		return true;
	}

}
