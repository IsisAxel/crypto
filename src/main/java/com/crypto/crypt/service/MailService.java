package com.crypto.crypt.service;

import org.entityframework.mail.MessageMail;
import org.entityframework.mail.ServerMail;
import org.entityframework.mail.ServiceMail;
import org.entityframework.mail.UserMail;

import com.crypto.crypt.model.Utilisateur;
import com.crypto.crypt.model.tiers.Feedback;

public class MailService {
    public static void sendEmail(String target, String content) throws Exception {
        UserMail sender = new UserMail("tonnybryan007@gmail.com", "yioo ffxs qwlp jylo");
        ServerMail serverMail = new ServerMail("smtp.gmail.com", "587");

        MessageMail message = new MessageMail();
        message.setContent(content);
        message.setSubject("CRYPT-G");
        message.setContentType("text/html");

        ServiceMail.sendMessage(sender, target, message, serverMail); 
    }

    public static void sendEmail(Feedback feed, Utilisateur u) throws Exception {
        UserMail sender = new UserMail("tonnybryan007@gmail.com", "yioo ffxs qwlp jylo");
        ServerMail serverMail = new ServerMail("smtp.gmail.com", "587");

        MessageMail message = new MessageMail();
        message.setContent(feed.getContent());
        message.setSubject("FEED - " + u.getNom() + " - " + feed.getSubject());
        message.setContentType("text/plain");

        ServiceMail.sendMessage(sender, "tonnybryan007@gmail.com", message, serverMail); 
    }

    public static String generateEmailHtml(String confirmationCode) {
        String htmlTemplate = 
                "<div style=\"margin: 0; padding: 0; background: linear-gradient(135deg, #f5f7fa, #c3cfe2); font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; height: 80vh;\">" +
                    "<div class=\"card\" style=\"background: #fff; border-radius: 15px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2); max-width: 400px; width: 100%; text-align: center; padding: 20px; margin:auto; height: fit-content;\">" +
                        "<h3 style=\"font-weight: bold; color: #f3ba2f; margin-bottom: 15px; font-size: xx-large;\">Crypt-G</h3>" +
                        "<div style=\"width: 80px; height: 80px; margin: 0 auto 15px;\">" +
                        "<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Binance_Logo.svg/768px-Binance_Logo.svg.png\" alt=\"Logo Crypt-G\" style=\"width: 80px; height: 80px; margin: 0 auto 15px;\" />" +
                        "</div>" +
                        "<h5 class=\"card-title\" style=\"font-weight: bold; font-size: 1.5rem; margin-bottom: 15px;\">Confirmation Code</h5>" +
                        "<div style=\"background: #f9f9f9; padding: 10px; border-radius: 5px; border: 1px solid #ddd; margin-bottom: 15px; display: inline-block; font-weight: bold; font-size: 1.2rem;\" id=\"confirmation-code\">" + confirmationCode + "</div>" +
                    "</div>" +
                "</div>";
        
        return htmlTemplate;
    }
}
