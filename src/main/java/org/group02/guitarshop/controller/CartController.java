package org.group02.guitarshop.controller;

import org.group02.guitarshop.entity.DiscountCode;
import org.group02.guitarshop.entity.Message;
import org.group02.guitarshop.entity.Invoice;
import org.group02.guitarshop.entity.InvoiceDetail;
import org.group02.guitarshop.entity.Product;
import org.group02.guitarshop.models.CartItemModel;
import org.group02.guitarshop.service.DiscountCodeService;
import org.group02.guitarshop.service.InvoiceDetailService;
import org.group02.guitarshop.service.InvoiceService;
import org.group02.guitarshop.service.CommentService;
import org.group02.guitarshop.service.ProductService;
import org.group02.guitarshop.models.PersonalInformation;
import org.group02.guitarshop.models.WishListItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountCodeService discountCodeService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @RequestMapping(value = "/gio-hang")
    public String viewDetail(Model model) {
        DiscountCode discountCode = new DiscountCode();
        model.addAttribute("discountCode",discountCode);
        return "/main/cart-detail";
    }
    @RequestMapping(value = "/wish-list")
    public String viewWishList(Model model) {
        DiscountCode discountCode = new DiscountCode();
        model.addAttribute("discountCode",discountCode);
        return "/main/wish-list";
    }

    @RequestMapping(value = "/add-to-cart", method = RequestMethod.GET)
    public String addToCart(HttpSession session, @RequestParam("productId") int productId, @RequestParam("quantity") int quantity) {
        HashMap<Integer, CartItemModel> sessionCart = (HashMap<Integer, CartItemModel>) session.getAttribute("sessionCart");
        if (sessionCart == null) {
            sessionCart = new HashMap<>();
        }

        Product product = productService.getProductById(productId);

        if (product != null) {
            if (sessionCart.containsKey(productId)) {
                CartItemModel item = sessionCart.get(productId);
                item.setProduct(product);
                item.setQuantity(item.getQuantity() + quantity);
                sessionCart.put(productId, item);
            } else {
                CartItemModel item = new CartItemModel();
                item.setProduct(product);
                item.setQuantity(quantity);
                sessionCart.put(productId, item);
            }
        }

        session.setAttribute("sessionCart", sessionCart);
        session.setAttribute("sessionCartNum", sessionCart.size());
        session.setAttribute("sessionCartTotal", getTotalPrice(sessionCart));

        return "redirect:/gio-hang";
    }
    @RequestMapping(value = "/add-to-wishlist", method = RequestMethod.GET)
    public String addToWishList(HttpSession session, @RequestParam("productId") int productId) {
        HashMap<Integer, WishListItemModel> sessionWishList = (HashMap<Integer, WishListItemModel>) session.getAttribute("sessionWishList");

        if (sessionWishList == null) {
            sessionWishList = new HashMap<>();
        }

        Product product = productService.getProductById(productId);

        if (product != null) {
            if (sessionWishList.containsKey(productId)) {
                WishListItemModel item = sessionWishList.get(productId);
                item.setProduct(product);
                sessionWishList.put(productId, item);
            } else {
                WishListItemModel item = new WishListItemModel();
                item.setProduct(product);
                sessionWishList.put(productId, item);
            }
        }

        session.setAttribute("sessionWishList", sessionWishList);
        session.setAttribute("sessionWishListNum", sessionWishList.size());

        return "redirect:/wish-list";
    }

    @RequestMapping(value = "/remove-from-cart", method = RequestMethod.GET)
    public String removeFromCart(HttpSession session, @RequestParam("productId") int productId) {
        HashMap<Integer, CartItemModel> sessionCart = (HashMap<Integer, CartItemModel>) session.getAttribute("sessionCart");

        if (sessionCart == null) {
            sessionCart = new HashMap<>();
        }

        sessionCart.remove(productId);

        if (sessionCart.size() == 0) {
            session.setAttribute("sessionCart", null);
            session.setAttribute("sessionCartNum", null);
            session.setAttribute("sessionCartTotal", null);
            session.setAttribute("sessionDiscountCode", null);
        } else {
            session.setAttribute("sessionCart", sessionCart);
            session.setAttribute("sessionCartNum", sessionCart.size());
            session.setAttribute("sessionCartTotal", getTotalPrice(sessionCart));
        }

        return "redirect:/gio-hang";
    }
    @RequestMapping(value = "/remove-from-wish-list", method = RequestMethod.GET)
    public String removeFromWishList(HttpSession session, @RequestParam("productId") int productId) {
        HashMap<Integer, CartItemModel> sessionWishList = (HashMap<Integer, CartItemModel>) session.getAttribute("sessionWishList");

        if (sessionWishList == null) {
            sessionWishList = new HashMap<>();
        }

        sessionWishList.remove(productId);

        if (sessionWishList.size() == 0) {
            session.setAttribute("sessionWishList", null);
            session.setAttribute("sessionCartNum", null);
            session.setAttribute("sessionDiscountCode", null);
        } else {
            session.setAttribute("sessionWishList", sessionWishList);
            session.setAttribute("sessionCartNum", sessionWishList.size());
        }

        return "redirect:/wish-list";
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.GET)
    public @ResponseBody String updateCart(HttpSession session, HttpServletRequest request) {

        String productIdString = request.getParameter("productId");
        String quantityString = request.getParameter("quantity");

        Integer productId = Integer.parseInt(productIdString);
        Integer quantity = Integer.parseInt(quantityString);

        HashMap<Integer, CartItemModel> sessionCart = (HashMap<Integer, CartItemModel>) session.getAttribute("sessionCart");

        if (sessionCart.containsKey(productId)) {
            CartItemModel item = sessionCart.get(productId);
            item.setQuantity(quantity);
            sessionCart.put(productId, item);
        }

        session.setAttribute("sessionCart", sessionCart);
        session.setAttribute("sessionCartNum", sessionCart.size());
        session.setAttribute("sessionCartTotal", getTotalPrice(sessionCart));

        return "1";
    }

    @RequestMapping(value="/comments", method=RequestMethod.POST)
    public String createComment(HttpSession session, @ModelAttribute("comment") Message comment, Model model){
        Product product = (Product) session.getAttribute("product");
        session.removeAttribute("product");
        comment.setProduct(product);
        commentService.insertComment(comment);
        int id = product.getId();

        model.addAttribute("product", productService.getProductById(id));
        productService.GetProductExtraInfo(id);
        model.addAttribute("TotalRate", productService.getTotalRate());
        model.addAttribute("ListCountRate", productService.getListCountRate());
        model.addAttribute("ListRelativeProduct", productService.getListRelatedProducts());
        model.addAttribute("AverageRate", productService.getAverageRate());
        model.addAttribute("comment", new Message("", "", "", product));
        return "redirect:/chi-tiet/?id="+product.getId();
    }

    @RequestMapping(value="/apply-discount-code", method=RequestMethod.POST)
    public String applyDiscountCode(HttpSession session, @ModelAttribute("discountCode") DiscountCode discountCode){
        removeDiscountCode(session);

        DiscountCode newDiscountCode = discountCodeService.getDiscountCodeByCode(discountCode.getCode());
        if (newDiscountCode != null) {
            session.setAttribute("sessionDiscountCode", newDiscountCode);
        }

        return "redirect:/gio-hang";
    }

    @RequestMapping(value="/remove-discount-code", method=RequestMethod.GET)
    public String removeDiscountCode(HttpSession session, @ModelAttribute("discountCode") DiscountCode discountCode){
        removeDiscountCode(session);
        return "redirect:/gio-hang";
    }

    @RequestMapping(value = "/thanh-toan", method = RequestMethod.GET)
    public String checkout() {

        return "/main/checkout";
    }

    @RequestMapping(value = "/thanh-toan", method = RequestMethod.POST)
    public String checkout(HttpSession session, Model model, @RequestBody Invoice requestInvoice) {

        HashMap<Integer, CartItemModel> sessionCart = (HashMap<Integer, CartItemModel>) session.getAttribute("sessionCart");
        PersonalInformation personalInformation = (PersonalInformation) session.getAttribute("PersonalInformation");
        DiscountCode discountCode = (DiscountCode) session.getAttribute("sessionDiscountCode");

        Invoice invoice = new Invoice();
        invoice.setCreatedTime(new Timestamp(new Date().getTime()));
        invoice.setStatus(1);

        if (discountCode != null) {
            invoice.setIdDiscountCode(discountCode.getId());
            invoice.setTotal(getTotalPrice(sessionCart) * (100 - discountCode.getDiscountAmount()) / 100);
        }
        invoice.setTotal(getTotalPrice(sessionCart));
        if (personalInformation != null)
            invoice.setIdPerson(personalInformation.getId());
        invoice.setCustomerName(requestInvoice.getCustomerName());
        invoice.setCustomerEmail(requestInvoice.getCustomerEmail());
        invoice.setCustomerPhone(requestInvoice.getCustomerPhone());
        invoice.setCustomerAddress(requestInvoice.getCustomerAddress());
        invoice.setCustomerMessage(requestInvoice.getCustomerMessage());
        invoice.setPaymentMethod(requestInvoice.getPaymentMethod());

        Integer invoiceId = invoiceService.insertInvoice(invoice);

        for (Map.Entry<Integer, CartItemModel> entry : sessionCart.entrySet()) {
            Product product = entry.getValue().getProduct();
            int quantity = entry.getValue().getQuantity();
            productService.updateQuantity(product.getId(), quantity);
            Integer key = entry.getKey();
            CartItemModel value = new CartItemModel(entry.getValue());
            InvoiceDetail invoiceDetail = new InvoiceDetail(invoiceId, value.getProduct().getId(), value.getQuantity(), false);
            invoiceDetail.setInvoiceByIdInvoice(invoiceService.getInvoiceById(invoiceId));
            invoiceDetail.setProductByIdProduct(productService.getProductById(value.getProduct().getId()));
            invoiceDetailService.insertInvoiceDetail(invoiceDetail);
        }
        session.setAttribute("sessionCartNum", 0);
        session.setAttribute("sessionCartTotal", 0);
        session.setAttribute("sessionCart", null);
        session.setAttribute("sessionDiscountCode", null);
        return "main/checkout";
    }

    private double getTotalPrice(HashMap<Integer, CartItemModel> sessionCart) {
        double sum = 0;

        for (Map.Entry<Integer, CartItemModel> list : sessionCart.entrySet()) {

            Product pd = list.getValue().getProduct();
            Integer qty = list.getValue().getQuantity();

            double discountedPrice = pd.getPrice() * (100 - pd.getDiscountAmount()) / 100;
            sum += discountedPrice * qty;
        }
        return sum;
    }

    private void removeDiscountCode(HttpSession session){
        session.setAttribute("sessionDiscountCode", null);
    }

}
