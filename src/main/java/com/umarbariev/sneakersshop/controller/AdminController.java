package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.dto.ClientOrdersStatusDto;
import com.umarbariev.sneakersshop.model.dto.SearchUserOrderDto;
import com.umarbariev.sneakersshop.model.dto.UpdateOrderStatusDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DOrderStatusDto;
import com.umarbariev.sneakersshop.security.UserDetailsServiceImpl;
import com.umarbariev.sneakersshop.service.ClientService;
import com.umarbariev.sneakersshop.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ClientService clientService;
    private final OrderService orderService;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/")
    public String getMainPage() {
        return "admin-page";
    }

    @GetMapping("/users")
    public String getClients(Model model) {
        List<ClientDto> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        model.addAttribute("searchUserOrders", new SearchUserOrderDto());
        return "admin-clients";
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<ClientOrdersStatusDto> orders = orderService.getAllOrders();
        List<DOrderStatusDto> statuses = orderService.getAllStatuses();
        List<String> clientUsernames = clientService.getAllClients().stream().map(ClientDto::getUsername).toList();
        model.addAttribute("clientsUsernames", clientUsernames);
        model.addAttribute("searchUserOrders", new SearchUserOrderDto());
        model.addAttribute("statuses", statuses);
        model.addAttribute("orders", orders);
        model.addAttribute("updateStatus", new UpdateOrderStatusDto());
        return "admin-orders";
    }

    @PostMapping("/orders")
    public String getOrdersByClient(@ModelAttribute("searchUserOrders") SearchUserOrderDto searchUserOrderDto, Model model) {
        List<ClientOrdersStatusDto> orders = orderService.getClientOrders(searchUserOrderDto.getUsername());
        List<DOrderStatusDto> statuses = orderService.getAllStatuses();
        List<String> clientUsernames = clientService.getAllClients().stream().map(ClientDto::getUsername).toList();
        model.addAttribute("statuses", statuses);
        model.addAttribute("orders", orders);
        model.addAttribute("searchUserOrders", new SearchUserOrderDto());
        model.addAttribute("clientsUsernames", clientUsernames);
        model.addAttribute("updateStatus", new UpdateOrderStatusDto());
        return "admin-orders";
    }

    @PostMapping("/orders/status")
    public String updateOrderStatus(@ModelAttribute("updateStatus") UpdateOrderStatusDto updateOrderStatusDto){
        orderService.updateOrderStatus(updateOrderStatusDto);
        return "redirect:/admin/orders";
    }

    @GetMapping("/block/{username}")
    public String blockUser(@PathVariable String username, HttpServletRequest request) {
        userDetailsService.blockUser(username);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/unblock/{username}")
    public String unblockUser(@PathVariable String username, HttpServletRequest request) {
        userDetailsService.unblockUser(username);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/client/{username}")
    public String getClientInfo(@PathVariable String username, Model model) {
        ClientDto clientDto = clientService.getClient(username);
        model.addAttribute("client", clientDto);
        model.addAttribute("searchUserOrders", new SearchUserOrderDto());
        return "admin-client-page";
    }
}
