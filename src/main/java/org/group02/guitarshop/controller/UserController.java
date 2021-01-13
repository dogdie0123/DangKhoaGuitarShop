package org.group02.guitarshop.controller;

import org.group02.guitarshop.entity.User;
import org.group02.guitarshop.others.Utility;
import org.group02.guitarshop.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value="/dang-ky", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("/main/register");
        return modelAndView;
    }

    @RequestMapping(value = "/dang-ky", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes ra)
            throws UnsupportedEncodingException, MessagingException {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "This email has been registered!!!");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/main/register");
        } else {
            userService.saveUser(user);
            String siteURL = Utility.getSiteURL(request);
            userService.sendVerificationEmail(user, siteURL);
            modelAndView.addObject("message", "Please check registered E-mail to Verify Acount!");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("main/login");
        }
        return modelAndView;
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code){
        boolean verified = userService.verify(code);
        return "main/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/admin/viewUsers")
    public String viewUser(Model model){
        List<User> userList = userService.listAll();
        model.addAttribute("userList", userList);
        return "admin/viewUsers";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal){
        String email = principal.getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "main/profile";
    }

    @RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
    public String saveProfile(@RequestParam("email") String email,
                               @RequestParam("name") String name,
                               @RequestParam("phone") String phone,
                               @RequestParam("address") String address,
                               RedirectAttributes ra){
        User user = userService.findUserByEmail(email);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        userService.save(user);
        ra.addFlashAttribute("message", "Profile Updated!!!");
        return "redirect:/profile";
    }

    @GetMapping("/changePassword")
    public String changePassword(Principal principal, Model model){
        String email = principal.getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "main/changePassword";
    }

    @RequestMapping(value = "/saveNewPassword", method = RequestMethod.POST)
    public String saveNewPassword(@RequestParam("email") String email,
                                  @RequestParam("oldPassword") String oldPassword,
                                  @RequestParam("newPassword") String newPassword,
                                  @RequestParam("retypePassword") String retypePassword,
                                RedirectAttributes ra){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userService.findUserByEmail(email);
        if(encoder.matches(oldPassword, user.getPassword())){
            if(newPassword == retypePassword){
                user.setPassword(encoder.encode(newPassword));
                userService.save(user);
                ra.addFlashAttribute("message", "Đã Cập nhật Password!!!");
            }else{
                ra.addFlashAttribute("error", "Mật khẩu mới không khớp!!!");
            }
        }else {
            ra.addFlashAttribute("error", "Mật khẩu hiện tại không chính xác!!!");
        }
        return "redirect:/changePassword";
    }
}
