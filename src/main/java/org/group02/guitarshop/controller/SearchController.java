package org.group02.guitarshop.controller;

import org.group02.guitarshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/tim-kiem", method = RequestMethod.GET)
    public String Search( Model model,
                          @RequestParam(value = "tu-khoa") String searchString,
                          @RequestParam(value="page", defaultValue = "1", required = false) int pageNumber) {
        PagedListHolder<?> pageListProduct = new PagedListHolder<>(productService.searchProducts((searchString)));
        pageListProduct.setPageSize(12);
        final int goToPage = pageNumber - 1;
        if (goToPage <= pageListProduct.getPageCount() && goToPage >= 0) {
            pageListProduct.setPage(goToPage);
        }
        model.addAttribute("tukhoa",searchString);
        model.addAttribute("result",productService.searchProducts(searchString));
        model.addAttribute("count",productService.searchProducts(searchString).size());

        int currentPage = pageListProduct.getPage() + 1;
        int beginPage = Math.max(1, currentPage - pageListProduct.getSource().size());
        int endPage = Math.min(beginPage + 4, pageListProduct.getPageCount());
        model.addAttribute("beginPage", beginPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageListProduct", pageListProduct);
        return "/main/search";
    }
}
