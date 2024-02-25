package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.SearchCriteria;
import com.umarbariev.sneakersshop.model.dto.dictionary.DBrandDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DCategoryDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import com.umarbariev.sneakersshop.service.BrandService;
import com.umarbariev.sneakersshop.service.CategoryService;
import com.umarbariev.sneakersshop.service.ShoeModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final ShoeModelService shoeModelService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @RequestMapping("")
    public String mainPage(Model model) {
        List<DShoeModelDto> shoeModelList = shoeModelService.getAllShoes();
        model.addAttribute("shoes", shoeModelList);
        return "main";
    }

    @GetMapping("search")
    public String getSearchPage(Model model) {
        List<DBrandDto> brands = brandService.getAllBrands();
        List<DCategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("searchCriteria", new SearchCriteria());
        return "search";
    }

    @PostMapping("search")
    public String search(@ModelAttribute("searchCriteria") SearchCriteria searchCriteria,
                         Model model) {
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(searchCriteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", searchCriteria);
        return "content";
    }
}
