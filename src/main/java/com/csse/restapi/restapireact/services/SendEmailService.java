package com.csse.restapi.restapireact.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public  void sendEmail(String to, String body, String topic){
        System.out.println ("sent email... 1");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage ();
        simpleMailMessage.setFrom ("kundelikwarner@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject (topic);
        simpleMailMessage.setText (body);

    javaMailSender.send (simpleMailMessage);
        System.out.println ("sent email... 2");
    }
    public  void sendEmailQr(String to, String body, String topic, String path) throws MessagingException {
        System.out.println ("sent email... 1");
    MimeMessage message = javaMailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    message.setContent (body,"text/html");
    helper.setFrom("kundelikwarner@gmail.com");
    helper.setTo(to);
    helper.setSubject(topic);

//
//    FileSystemResource file
//            = new FileSystemResource(new File (path));
//    helper.addAttachment("QR-code.png", file);


        MimeMultipart multipart = new MimeMultipart("related");
        // first part  (the html)
        BodyPart messageBodyPart = new MimeBodyPart ();
        String htmlText = "<div style='margin-left:220px'>Show this QR to manager in ticket-office: </div><br/><span><img src ='https://media1.tenor.com/images/d7bf2b7de2148472923eb978a735d74e/tenor.gif?itemid=17332108' style='' height='250' width = '250' > <img height='200' width='200' src=\"cid:image\"> <img src ='https://media1.tenor.com/images/d7bf2b7de2148472923eb978a735d74e/tenor.gif?itemid=17332108' style='' height='250' width = '250' > </span> <br/>" + body ;
        messageBodyPart.setContent(htmlText, "text/html");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource
                (path);
        messageBodyPart.setDataHandler(new DataHandler (fds));
        messageBodyPart.setHeader("Content-ID","<image>");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // put everything together
        message.setContent(multipart);

        javaMailSender.send(message);
        System.out.println ("sent email... 2");
    }



}
