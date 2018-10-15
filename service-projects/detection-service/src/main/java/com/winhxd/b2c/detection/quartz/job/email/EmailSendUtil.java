package com.winhxd.b2c.detection.quartz.job.email;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chen
 * @date 2018/09/30
 */
@Configuration
public class EmailSendUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSendUtil.class);

    /**
     * 服务邮箱(from邮箱)
     */
    @Value("${mail.username}")
    private String username;

    /**
     * 邮箱密码
     */
    @Value("${mail.password}")
    private String password;

    /**
     * 发件人昵称
     */
    @Value("${mail.senderNick}")
    private String senderNick;

    /**
     * smtp邮件服务器
     */
    @Value("${mail.smtp.host}")
    private String mailHost;

    /**
     * 邮件服务器默认的端口
     */
    @Value("${mail.smtp.port}")
    private String mailPort;

    /**
     * 尝试使用auth命令认证用户
     */
    @Value("${mail.smtp.auth}")
    private String auth;

    /**
     * 服务的协议
     */
    @Value("${mail.transport.protocol}")
    private String protocol;
    @Value("${mail.smtp.timeout}")
    private Integer timeout;

    /**
     * 系统属性
     */
    private static Properties props;
    /**
     * 邮件会话对象
     */
    private static Session session;
    /**
     * MIME邮件对象
     */
    private static MimeMessage mimeMsg;
    /**
     * Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
     */
    private static Multipart mp;

    /**
     *预编译邮箱校验正则
     */
    private static  Pattern compile = Pattern.compile("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");

    /**
     * 邮件会话对象
     *
     * @return 配置好的工具
     */
    private Session createMailSender() {
        props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.host", mailHost);
        props.put("mail.smtp.port", mailPort);
        props.put("mail.smtp.timeout",timeout);
        props.put("username", username);
        props.put("password", password);
        // 建立会话
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
        return session;
    }

    /**
     * 发送邮件
     * @param to 收件人, 多个Email以英文逗号分隔
     * @param cc 抄送, 多个Email以英文逗号分隔
     * @param subject 主题
     * @param content 内容
     * @return
     */
    public void sendMail(String to, String cc, String subject, String content) {
        try {
            mimeMsg = new MimeMessage(createMailSender());
            mp = new MimeMultipart();
            // 发件人昵称
            String nick = senderNick;
            try {
                nick = MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
            mimeMsg.setFrom(new InternetAddress(username, nick));
            // 设置收件人
            //正则校验收件人的邮箱的合法性

            Matcher matcher = compile.matcher(to);
            boolean matches = matcher.matches();
            if(!matches){
                LOGGER.warn("监控功能的收件人:{},不合法。",to);
            }
            if(StringUtils.isNotBlank(to)){
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }else {
                LOGGER.warn("发送邮件的收件人为空，无法发送");
            }
            // 设置抄送人
            if(StringUtils.isNotBlank(cc)){
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart();
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
//            if (fileList != null && fileList.length > 0) {
//                for (int i = 0; i < fileList.length; i++) {
//                    bp = new MimeBodyPart();
//                    FileDataSource fds = new FileDataSource(fileList[i]);
//                    bp.setDataHandler(new DataHandler(fds));
//                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
//                    mp.addBodyPart(bp);
//                }
//            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            // 发送邮件
            Object object = props.get("mail.smtp.auth");
            if (("true").equals(object.toString())) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String)props.get("mail.smtp.host"), (String)props.get("username"), (String)props.get("password"));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }
            LOGGER.info("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            LOGGER.error("邮件发送异常，异常信息为",e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOGGER.error("邮件发送异常，异常信息为",e);
        }
    }

}

