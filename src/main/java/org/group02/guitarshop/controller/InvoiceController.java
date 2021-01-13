package org.group02.guitarshop.controller;

import org.group02.guitarshop.entity.*;
import org.group02.guitarshop.others.PDFExporter;
import org.group02.guitarshop.service.InvoiceDetailService;
import org.group02.guitarshop.service.InvoiceService;
import org.group02.guitarshop.service.ProductService;
import org.group02.guitarshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Controller
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @Autowired
    InvoiceDetailService invoiceDetailService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/admin/viewInvoices")
    public String viewInvoices(Model model,
                               @Param(value = "status") String status) {
        if (status == null) {
            List<Invoice> invoiceList = invoiceService.listAll();
            model.addAttribute("status", status);
            model.addAttribute("invoiceList", invoiceList);
        } else {
            List<Invoice> invoiceList = invoiceService.listByStatus(Integer.parseInt(status));
            model.addAttribute("status", status);
            model.addAttribute("invoiceList", invoiceList);
        }
        return "admin/viewInvoices";
    }

    @GetMapping("/admin/viewInvoiceDetail/{id}")
    public ModelAndView viewInvoiceDetail(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("admin/invoiceDetail");
        Invoice invoice = invoiceService.getInvoiceById(id);
        List<InvoiceDetail> invoiceDetails = invoice.getInvoiceDetailsById();
        mav.addObject("invoice", invoice);
        mav.addObject("invoiceDetails", invoiceDetails);
        return mav;
    }

    @GetMapping("/admin/setInvoiceStatus/{id}")
    public String setStatus(@PathVariable(name = "id") int id,
                          @Param(value = "status" ) String status,
                          RedirectAttributes ra) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        invoice.setStatus(Integer.parseInt(status));
        invoiceService.save(invoice);
        if(status.equals("4")){
            for(InvoiceDetail item : invoice.getInvoiceDetailsById()){
                int productId = item.getProductByIdProduct().getId();
                int quantity = item.getQuantity();
                productService.updateQuantityWhenCancelOrder(productId,quantity);
            }
        }
        String a;
        if (status.equals("1")){
            a = "Pending";
        }else if(status.equals("2")){
            a = "Shipping";
        }else if(status.equals("3")){
            a = "Completed";
        }else {
            a = "Cancelled";
        }
        ra.addFlashAttribute("message","Status of the order is changed to "+a);
        return "redirect:/admin/viewInvoiceDetail/"+id;
    }

    @GetMapping("/viewOrders")
    public String viewOrders(Model model,
                             Principal principal,
                             @Param(value = "status") String status) {
        String email = principal.getName();
        User user = userService.findUserByEmail(email);
        int userID = user.getUser_Id();

        if (status == null) {
            List<Invoice> invoiceList = invoiceService.listAllByUserID(userID);
            model.addAttribute("status", status);
            model.addAttribute("invoiceList", invoiceList);
        } else {
            List<Invoice> invoiceList = invoiceService.listAllByUserIDAndStatus(userID, Integer.parseInt(status));
            model.addAttribute("status", status);
            model.addAttribute("invoiceList", invoiceList);
        }
        return "main/viewInvoices";
    }

    @GetMapping("/viewInvoiceDetail/{id}")
    public ModelAndView viewInvoiceDetailByUser(@PathVariable(name = "id") int id,
                                                Authentication authentication) {
        String email = authentication.getName();
        User user  = userService.findUserByEmail(email);
        Invoice invoice = invoiceService.findByInvoiceIdAndUserId(id, user.getUser_Id());
        if(invoice == null){
            ModelAndView mav = new ModelAndView("error/403");
            return mav;
        }else {
            ModelAndView mav = new ModelAndView("main/invoiceDetail");
            List<InvoiceDetail> invoiceDetails = invoice.getInvoiceDetailsById();
            mav.addObject("invoice", invoice);
            mav.addObject("invoiceDetails", invoiceDetails);
            return mav;
        }
    }

    @GetMapping(value = "/admin/report")
    public String exportReport() {
        return "admin/exportReport";
    }

    @RequestMapping(value = "/admin/exportByDay/{date}", method = RequestMethod.GET)
    public void exportPDF(HttpServletResponse response,
                          @PathVariable(name = "date") java.sql.Date date) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=incomeReport_" + dateFormat.format(date) + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<Invoice> invoiceList = invoiceService.listByStatus23(date);
        Optional<Float> sum = invoiceService.invoiceSubTotalByDate(date);
        float result=0;
        if(sum.isPresent()){
            result = sum.get();
        }
        PDFExporter exporter = new PDFExporter(invoiceList, result, date);
        exporter.exportByDate(response);
    }

    @RequestMapping(value = "/admin/exportFromTo", method = RequestMethod.GET)
    public void exportPDF(HttpServletResponse response,
                          @Param(value = "startDate") java.sql.Date startDate,
                          @Param(value = "endDate") java.sql.Date endDate) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Report_From_" + dateFormat.format(startDate) + "_To_" + dateFormat.format(endDate) + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<Invoice> invoiceList = invoiceService.listByStatus23FromTo(startDate, endDate);
        Optional<Float> sum = invoiceService.invoiceSubTotalFromTo(startDate, endDate);
        float result=0;
        if(sum.isPresent()){
            result = sum.get();
        }
        PDFExporter exporter = new PDFExporter(invoiceList, result, startDate, endDate);
        exporter.exportFromTo(response);
    }
}
