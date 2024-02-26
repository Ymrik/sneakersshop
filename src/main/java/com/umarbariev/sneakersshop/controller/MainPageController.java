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
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("searchPage")
    public String getSearchPage(Model model) {
        List<DBrandDto> brands = brandService.getAllBrands();
        List<DCategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("searchCriteria", new SearchCriteria());
        return "search";
    }

    @PostMapping("searchPage")
    public String getSearchPage(@ModelAttribute("searchCriteria") SearchCriteria searchCriteria, Model model) {
        List<DBrandDto> brands = brandService.getAllBrands();
        List<DCategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("searchCriteria", searchCriteria);
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

    @GetMapping("men")
    public String getMenContent(Model model) {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setSex("M");
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(criteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", criteria);
        return "content";
    }

    @GetMapping("women")
    public String getWomenContent(Model model) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setSex("F");
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(searchCriteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", searchCriteria);
        return "content";
    }

    @GetMapping("children")
    public String getChildrenContent(Model model) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAgeCategory(SearchCriteria.CHILDREN);
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(searchCriteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", searchCriteria);
        return "content";
    }

    @GetMapping("winter")
    public String getWinterContent(Model model) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setSeason("WINTER");
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(searchCriteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", searchCriteria);
        return "content";
    }

    @GetMapping("summer")
    public String getSummerContent(Model model) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setSeason("SUMMER");
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(searchCriteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", searchCriteria);
        return "content";
    }

    @GetMapping("spring")
    public String getSpringAutumnContent(Model model) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setSeason("AUT/SPR");
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(searchCriteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", searchCriteria);
        return "content";
    }

    @GetMapping("demi")
    public String getDemiSeasonContent(Model model) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setSeason("DEMI");
        List<DShoeModelDto> shoeModelList = shoeModelService.findShoes(searchCriteria);
        model.addAttribute("shoes", shoeModelList);
        model.addAttribute("searchCriteria", searchCriteria);
        return "content";
    }

    @GetMapping("/shoe/{id}")
    public String getShoeModelPage(@PathVariable Long id, Model model) {
        DShoeModelDto shoeModelDto = shoeModelService.getShoeById(id);
        model.addAttribute("shoe", shoeModelDto);
        return "model-info";
    }
}
