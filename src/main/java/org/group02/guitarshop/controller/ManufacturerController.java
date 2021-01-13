package org.group02.guitarshop.controller;

import org.group02.guitarshop.entity.Manufacturer;
import org.group02.guitarshop.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping("/admin/viewManufacturers")
    public String viewManufacfurers(Model model){
        List<Manufacturer> manufacturerList = manufacturerService.listAll();
        model.addAttribute("manufacturerList", manufacturerList);
        return "admin/viewManufacturers";
    }

    @GetMapping("/admin/createManufacturer")
    public String createManufacturer(Model model){
        Manufacturer manufacturer = new Manufacturer();
        model.addAttribute("manufacturer", manufacturer);
        return "admin/createManufacturer";
    }

    @PostMapping("/saveManufacturer")
    public String saveManufacturer(@ModelAttribute("manufacturer") Manufacturer manufacturer, RedirectAttributes ra){
        manufacturerService.save(manufacturer);
        ra.addFlashAttribute("message", "Đã lưu Nhà sản xuất");
        return "redirect:admin/viewCategories";
    }

    @GetMapping("/updateManufacturer/{id}")
    public ModelAndView updateManufacturer(@PathVariable(name = "id") int id){
        ModelAndView mav = new ModelAndView("admin/updateManufacturer");
        Manufacturer manufacturer = manufacturerService.getManufacturerById(id);
        mav.addObject("manufacturer", manufacturer);
        return mav;
    }

    @GetMapping("/deleteManufactuer/{id}")
    public String deleteManufacturer(@PathVariable(name = "id") int id, RedirectAttributes ra){
        manufacturerService.delete(id);
        ra.addFlashAttribute("message", "Đã xóa NSX có ID = "+id);
        return "redirect:/admin/viewCategories";
    }
}
