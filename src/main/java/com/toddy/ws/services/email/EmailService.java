package com.toddy.ws.services.email;

import com.toddy.ws.model.User;
import com.toddy.ws.model.VerificationToken;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    void sendHtmlEmail(MimeMessage msg);
    void sendConfirmationHtmlEmail(User user, VerificationToken vToken);

}
