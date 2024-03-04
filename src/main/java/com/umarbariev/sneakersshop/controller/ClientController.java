package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.*;
import com.umarbariev.sneakersshop.model.entity.UserEntity;
import com.umarbariev.sneakersshop.security.UserDetailsServiceImpl;
import com.umarbariev.sneakersshop.service.ClientService;
import com.umarbariev.sneakersshop.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final OrderService orderService;
    private final UserDetailsServiceImpl userDetailsService;

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

    @GetMapping("/edit")
    public String changeAccountData(Principal principal, Model model) {
        ClientDto clientDto = clientService.getClient(principal.getName());
        model.addAttribute("client", clientDto);
        return "change-account";
    }

    @PostMapping("/edit")
    public String updateAccountData(@ModelAttribute(name = "client") ClientDto clientDto) {
        clientService.updateClient(clientDto);
        return "redirect:/client/account";
    }

    @GetMapping("/change-password")
    public String changePassword(Principal principal, Model model) {
        UpdateUserPasswordDto user = new UpdateUserPasswordDto();
        user.setUsername(principal.getName());
        model.addAttribute("user", user);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("user") UpdateUserPasswordDto updateUserPasswordDto,
                                 Principal principal,
                                 Model model) {
        try {
            userDetailsService.updateUser(updateUserPasswordDto, principal);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return changePassword(principal, model);
        }
        return "redirect:/logout";
    }

    @GetMapping("/favourites/add/{shoeId}")
    public String addToFavourites(@PathVariable Long shoeId, Principal principal, HttpServletRequest request) {
        String username = principal.getName();
        clientService.addShoeToFavourite(username, shoeId);
        String referer = request.getHeader("Referer");
        if (referer.contains("/login")) {
            return "redirect:/";
        }
        return "redirect:" + referer;
    }

    @GetMapping("/favourites/delete/{shoeId}")
    public String deleteFromFavourites(@PathVariable Long shoeId, Principal principal, HttpServletRequest request) {
        String username = principal.getName();
        clientService.deleteShoeFromFavourites(username, shoeId);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/favourites")
    public String getFavourites(Model model, Principal principal) {
        ClientFavouritesDto clientFavouritesDto = clientService.getClientFavourites(principal.getName());
        model.addAttribute("clientFavourites", clientFavouritesDto);
        return "client-favourites";
    }
}
