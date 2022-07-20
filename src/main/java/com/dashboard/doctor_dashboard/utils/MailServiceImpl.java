package com.dashboard.doctor_dashboard.utils;

import com.dashboard.doctor_dashboard.exceptions.MailErrorException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class MailServiceImpl {

    private JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * This Function of service is to send mail service handle object to mailer
     * @param fromEmail
     * @param toEmail
     * @param senderName
     * @param subject
     * @param content
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void mailServiceHandler(String fromEmail, String toEmail, String senderName, String subject , String content) throws MessagingException, JSONException, UnsupportedEncodingException {
        var obj = new JSONObject();
        obj.put("fromEmail", fromEmail);
        obj.put("toEmail", toEmail);
        obj.put("senderName", senderName);
        obj.put("subject", subject);
        obj.put("content", content);
        sendMailer(obj);
    }


    /**
     * This function of service is for sending mail to user.
     * @param obj which contains fields fromEmail,toEmail,senderName,Subject etc
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    private void sendMailer(JSONObject obj) throws MessagingException, JSONException, UnsupportedEncodingException {

        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setFrom(obj.get("fromEmail").toString(), obj.get("senderName").toString());
            helper.setTo(obj.get("toEmail").toString());
            helper.setText(obj.get("content").toString(), true);
            helper.setSubject(obj.get("subject").toString());
            mailSender.send(message);
            log.info(" Appointment Mail Service Stopped!!");

        }catch (Exception e)
        {
            log.info(Constants.MAIL_ERROR);
            log.info("Exception!!! Mail Service Stopped!!");
            throw new MailErrorException(Constants.MAIL_ERROR);
        }
  }
}
