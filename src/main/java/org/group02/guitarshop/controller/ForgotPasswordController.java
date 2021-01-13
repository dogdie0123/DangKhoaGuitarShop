package org.group02.guitarshop.controller;

import net.bytebuddy.utility.RandomString;
import org.group02.guitarshop.entity.User;
import org.group02.guitarshop.others.Utility;
import org.group02.guitarshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "main/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model, RedirectAttributes ra) throws UnsupportedEncodingException, MessagingException {
        String email = request.getParameter("email");
        User user = userService.findUserByEmail(email);
        if(user == null){
            ra.addFlashAttribute("error","No account registered with this Email!!!");
        }
        else {
            String token = RandomString.make(30);
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            ra.addFlashAttribute("message", "We've sent reset password link to your Email!!!");
        }
        return "redirect:/forgot_password";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("guitarshop@gmail.com", "GuitarShop");
        helper.setTo(recipientEmail);

        String subject = "Guitar Shop - Reset Password";

        String content = "<p>Hello,</p>"
                + "<p>We've received your reset password request.</p>"
                + "<p>Clink the following link to reset your password: </p>"
                + "<p><a href=\"" + link + "\">Reset Password</a></p>"
                + "<br>"
                + "<p>Skip this Email if you've already logged in or you haven't sent this request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        if (user == null) {
            model.addAttribute("message", "Token doesn't exist!!!");
            return "main/message";
        }
        return "main/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset Password");

        if (user == null) {
            model.addAttribute("message", "Token doesn't exist!!!");
            return "main/reset_fail";
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "Your password is Reset!!!");
        }
        return "main/reset_success";
    }
}