package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.entity.ClientEntity;
import com.umarbariev.sneakersshop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/account")
    public String personalAccount(Model model, Principal principal) {
        String username = principal.getName();
        ClientDto client = clientService.getClient(username);
        model.addAttribute("client", client);

        return "account";
    }
}
