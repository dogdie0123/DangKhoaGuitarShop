package org.group02.guitarshop.controller;

import org.group02.guitarshop.entity.InvoiceDetail;
import org.group02.guitarshop.entity.Rate;
import org.group02.guitarshop.entity.User;
import org.group02.guitarshop.repository.RateRepository;
import org.group02.guitarshop.repository.UserRepository;
import org.group02.guitarshop.service.InvoiceDetailService;
import org.group02.guitarshop.service.RateService;
import org.group02.guitarshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RateController {
    @Autowired
    InvoiceDetailService invoiceDetailService;
    @Autowired
    RateService rateService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/rateProduct/", method = RequestMethod.GET)
    public String rateProduct(Authentication authentication,
                              @RequestParam(name="star") String star,
                              @RequestParam(name ="review") String review,
                              @RequestParam(name="iId") String iId,
                              @RequestParam(name="pId") String pId,
                              RedirectAttributes ra){
        User user = userService.findUserByEmail(authentication.getName());
        String name = user.getName();
        InvoiceDetail detail = invoiceDetailService.findById(Integer.parseInt(iId), Integer.parseInt(pId));
        Rate rate = new Rate();
        rate.setName(name);
        rate.setIdProduct(Integer.parseInt(pId));
        rate.setNumberOfStars(Integer.parseInt(star));
        rate.setContent(review);
        rateService.save(rate);
        detail.setReviewed(true);
        invoiceDetailService.save(detail);
        ra.addFlashAttribute("message", "Thanks for rating our Product!!!");
        return "redirect:/viewInvoiceDetail/"+iId;
    }
}
