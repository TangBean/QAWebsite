package com.demo.util;

import com.demo.model.ViewObject;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(MailSender.class);

    private JavaMailSenderImpl mailSender;

    @Autowired
    private Configuration freeMakerConfiguration;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                     String templateName, Map<String, Object> model) {
        try {
            // 准备需要的发送邮件的内容信息
            String nick = MimeUtility.encodeText("低仿知乎");
            InternetAddress from = new InternetAddress(nick + "<tang_bean@163.com>");
            String context = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freeMakerConfiguration.getTemplate(templateName), model);

            // 把准备好的邮件信息设置好
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(context);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败：" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("tang_bean@163.com");
        mailSender.setPassword("tang4438");
        mailSender.setProtocol("smtp");
        mailSender.setHost("smtp.163.com");
        mailSender.setPort(465);
        mailSender.setDefaultEncoding("utf-8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
//        javaMailProperties.put("mail.smtp.socketFactory.fallback", "true");
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
