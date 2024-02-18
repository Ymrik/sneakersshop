package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.dto.UserDto;
import com.umarbariev.sneakersshop.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final ClientService clientService;

    @RequestMapping("")
    public String register(Model model) {
        ClientDto clientDto = new ClientDto();
        model.addAttribute("client", clientDto);

        return "registration";
    }

    @PostMapping("")
    public String register(@Valid @ModelAttribute("client") ClientDto client, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("client", client);
            return "registration";
        }
        clientService.saveNewClient(client);
        return "login";
    }
}
