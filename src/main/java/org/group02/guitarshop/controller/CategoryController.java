package org.group02.guitarshop.controller;

import org.group02.guitarshop.entity.Category;
import org.group02.guitarshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/viewCategories")
    public String viewCategories(Model model){
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "admin/viewCategories";
    }

    @GetMapping("/admin/createCategory")
    public String createCategory(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "admin/createCategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") Category category, RedirectAttributes ra){
        categoryService.save(category);
        ra.addFlashAttribute("message", "Đã lưu danh mục");
        return "redirect:admin/viewCategories";
    }

    @GetMapping("/updateCategory/{id}")
    public ModelAndView updateCategory(@PathVariable(name = "id") int id){
        ModelAndView mav = new ModelAndView("admin/updateCategory");
        Category category = categoryService.getCategoryById(id);
        mav.addObject("category", category);
        return mav;
    }

    @RequestMapping(value = "/deleteCategory/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteCategory(@PathVariable(name = "id") int id, RedirectAttributes ra){
        categoryService.delete(id);
        ra.addFlashAttribute("message", "Đã xóa danh mục có ID = "+ id);
        return "redirect:/admin/viewCategories";
    }

    @RequestMapping(value = "/san-pham/{metadata}", method = RequestMethod.GET)
    public String Category(Model model,
                           @PathVariable(value="metadata") String metadata,
                           @RequestParam(value="manufacturer", defaultValue = "All", required = false) String manufacturer,
                           @RequestParam(value="style", defaultValue = "All", required = false) String style,
                           @RequestParam(value="minPrice", defaultValue = "0", required = false) int minPrice,
                           @RequestParam(value="maxPrice", defaultValue = "10000000", required = false) int maxPrice,
                           @RequestParam(value="page", defaultValue = "1", required = false) int pageNumber) {
        Category category = categoryService.getCategoryByMetadata(metadata);
        model.addAttribute("mMetadata", metadata);
        model.addAttribute("mManufacturer", manufacturer);
        model.addAttribute("mStyle", style);
        model.addAttribute("mMinPrice", minPrice);
        model.addAttribute("mMaxPrice", maxPrice);
        model.addAttribute("styleList", categoryService.getListOfStyleNames(category.getId()));
        model.addAttribute("manufacturerList", categoryService.getListOfManufacturerNames(category.getId()));

        PagedListHolder<?> pageListProduct = new PagedListHolder<>(categoryService.getListProduct(category.getId(), manufacturer, style, minPrice, maxPrice));
        pageListProduct.setPageSize(9);
        final int goToPage = pageNumber - 1;
        if (goToPage <= pageListProduct.getPageCount() && goToPage >= 0) {
            pageListProduct.setPage(goToPage);
        }
        int currentPage = pageListProduct.getPage() + 1;
        int beginPage = Math.max(1, currentPage - pageListProduct.getSource().size());
        int endPage = Math.min(beginPage + 4, pageListProduct.getPageCount());
        model.addAttribute("beginPage", beginPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageListProduct", pageListProduct);

        return "/main/category";
    }
}
