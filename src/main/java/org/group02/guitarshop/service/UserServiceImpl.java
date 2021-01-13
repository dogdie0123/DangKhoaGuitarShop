package org.group02.guitarshop.service;

import net.bytebuddy.utility.RandomString;
import org.group02.guitarshop.entity.Role;
import org.group02.guitarshop.entity.User;
import org.group02.guitarshop.repository.RoleRepository;
import org.group02.guitarshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                             RoleRepository roleRepository,
                             BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(int id) {
        return userRepository.findByUser_Id(id);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName("user");
        user.setEnable(false);
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String subject = "GuitarShop - Xác nhận tài khoản";
        String senderName = "GuitarShop";
        String mailContent = "<p>Xin chào " + user.getName() +",</p>";
        mailContent += "<p>Click vào link dưới đây để xác nhận tài khoản của bạn:</p>";
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        mailContent += "<h3><a href=\"" + verifyURL + "\">Verify</a></h3>";
        mailContent += "<p>Xin cảm ơn<br>GuitarShop Team</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("guitarshop@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);

        mailSender.send(message);
    }

    public boolean verify(String verificationCode){
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnable()){
            return false;
        }else {
            userRepository.enable(user.getUser_Id());
            return true;
        }
    }

    public void updateResetPasswordToken(String token, String email){
        User user = userRepository.findByEmail(email);
            user.setResetPasswordToken(token);
            userRepository.save(user);
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }
}