package org.group02.guitarshop.controller;

import org.group02.guitarshop.entity.Category;
import org.group02.guitarshop.entity.Manufacturer;
import org.group02.guitarshop.entity.Message;
import org.group02.guitarshop.entity.Product;
import org.group02.guitarshop.entity.User;
import org.group02.guitarshop.service.ProductService;
import org.group02.guitarshop.service.UserService;
import org.group02.guitarshop.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/chi-tiet", method = RequestMethod.GET)
    public String productDetail(HttpServletRequest request, HttpSession session, @RequestParam("id") int id, Model model) {
        Product product = productService.getProductById(id);
        session.setAttribute("product", product);
        model.addAttribute("product", product);
        productService.GetProductExtraInfo(id);
        model.addAttribute("TotalRate", productService.getTotalRate());
        model.addAttribute("ListCountRate", productService.getListCountRate());
        model.addAttribute("ListRelativeProduct", productService.getListRelatedProducts());
        model.addAttribute("AverageRate", productService.getAverageRate());
        model.addAttribute("ListComment", commentService.findAllCommentOfProduct(id));
        model.addAttribute("rateList", productService.listRateByProductId(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Message msg = new Message("", "", "", product);
        String email = auth.getName();
        if (auth != null && (!"anonymousUser".equals(email))) {
            User user = userService.findUserByEmail(email);
            msg.setEmail(auth.getName());
            msg.setName(user.getName());
        }
        model.addAttribute("comment", msg);
        return "main/product-detail";
    }

    @GetMapping("/admin/viewProducts")
    public String viewProduct(Model model) {
        List<Product> productList = productService.listAll();
        model.addAttribute("productList", productList);
        return "admin/viewProducts";
    }

    @GetMapping("/admin/viewComments")
    public String viewComment(Model model) {
        List<Message> commentList = commentService.findAllCommentNotApprove();
        model.addAttribute("commentList", commentList);
        return "admin/viewComments";
    }

    @GetMapping("/admin/viewCommentsApproved")
    public String viewCommentsApproved(Model model) {
        List<Message> commentList = commentService.findAllCommentApproved();
        model.addAttribute("commentList", commentList);
        return "admin/viewCommentsApproved";
    }

    @ModelAttribute(name = "CategoryList")
    public List<Category> listCategoryName(){
        return productService.listAllCategory();
    }

    @ModelAttribute(name = "ManufacturerList")
    public List<Manufacturer> listManufacturerName(){
        return productService.listAllManufacturer();
    }

    @GetMapping("/admin/createProduct")
    public String createMovie(ModelMap model){
        model.addAttribute("product", new Product());
        return "admin/addProduct";
    }

    @GetMapping("/admin/updateProduct/{id}")
    public ModelAndView updateProduct(@PathVariable(name = "id") int id){
        ModelAndView mav = new ModelAndView("/admin/updateProduct");
        Product product = productService.getProductById(id);
        mav.addObject("product", product);
        return mav;
    }

    @GetMapping("/admin/approveComment/{id}")
    public String approveComment(@PathVariable(name = "id") int commentId, Model model){
        commentService.approveComment(commentId);
        List<Message> commentList = commentService.findAllCommentNotApprove();
        model.addAttribute("commentList", commentList);

        return "admin/viewComments";
    }

    @GetMapping("/admin/deleteComment/{id}")
    public String deleteComment(@PathVariable(name = "id") int commentId, Model model){
        commentService.deleteComment(commentId);
        List<Message> commentList = commentService.findAllCommentNotApprove();
        model.addAttribute("commentList", commentList);

        return "admin/viewComments";
    }

    @GetMapping("/admin/deleteCommentApproved/{id}")
    public String deleteCommentApproved(@PathVariable(name = "id") int commentId, Model model){
        commentService.deleteComment(commentId);
        List<Message> commentList = commentService.findAllCommentApproved();
        model.addAttribute("commentList", commentList);

        return "admin/viewCommentsApproved";
    }

    @PostMapping("/admin/saveProduct")
    public String saveCategory(@ModelAttribute("product") Product product,
                               @RequestParam("fileImage")MultipartFile multipartFile , RedirectAttributes ra) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setImageThumbnail(fileName);
        Product savedProduct = productService.save(product);
        String uploadDir = "products/" + savedProduct.getId();
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try(InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Không Upload được File: " + fileName);
        }
        ra.addFlashAttribute("message", "Đã lưu Sản phẩm");
        return "redirect:/admin/viewProducts";
    }

    @GetMapping("/admin/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id, RedirectAttributes ra){
        productService.deleteById(id);
        ra.addFlashAttribute("message", "Đã xóa Sản phẩm có ID = "+id);
        return "redirect:/admin/viewProducts";
    }
}
