package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.dto.ClientOrdersStatusDto;
import com.umarbariev.sneakersshop.service.ClientService;
import com.umarbariev.sneakersshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final OrderService orderService;

    @GetMapping("/account")
    public String personalAccount(Model model, Principal principal) {
        String username = principal.getName();
        ClientDto client = clientService.getClient(username);
        model.addAttribute("client", client);

        return "account";
    }

    @GetMapping("/orders")
    public String viewOrders(Principal principal, Model model) {
        String username = principal.getName();
        List<ClientOrdersStatusDto> clientOrders = orderService.getClientOrders(username);
        model.addAttribute("clientOrders", clientOrders);
        return "client-orders";
    }
}
