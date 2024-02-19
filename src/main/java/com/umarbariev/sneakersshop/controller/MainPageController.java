package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import com.umarbariev.sneakersshop.service.ShoeModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final ShoeModelService shoeModelService;

    @RequestMapping("")
    public String mainPage(Model model) {
        List<DShoeModelDto> shoeModelList = shoeModelService.getAllShoes();
        model.addAttribute("shoes", shoeModelList);
        return "main";
    }
}
