package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.ClientDto;
import com.umarbariev.sneakersshop.model.dto.ClientOrdersStatusDto;
import com.umarbariev.sneakersshop.model.dto.UpdateOrderStatusDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DOrderStatusDto;
import com.umarbariev.sneakersshop.security.UserDetailsServiceImpl;
import com.umarbariev.sneakersshop.service.ClientService;
import com.umarbariev.sneakersshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return "admin-clients";
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<ClientOrdersStatusDto> orders = orderService.getAllOrders();
        List<DOrderStatusDto> statuses = orderService.getAllStatuses();
        model.addAttribute("statuses", statuses);
        model.addAttribute("orders", orders);
        model.addAttribute("updateStatus", new UpdateOrderStatusDto());
        return "admin-orders";
    }

    @PostMapping("/orders/status")
    public String updateOrderStatus(@ModelAttribute("updateStatus") UpdateOrderStatusDto updateOrderStatusDto){
        orderService.updateOrderStatus(updateOrderStatusDto);
        return "redirect:/admin/orders";
    }

    @GetMapping("/block/{username}")
    public String blockUser(@PathVariable String username) {
        userDetailsService.blockUser(username);
        return "redirect:/admin/users";
    }

    @GetMapping("/unblock/{username}")
    public String unblockUser(@PathVariable String username) {
        userDetailsService.unblockUser(username);
        return "redirect:/admin/users";
    }
}
