package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.*;
import com.umarbariev.sneakersshop.model.dto.dictionary.DBrandDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DCategoryDto;
import com.umarbariev.sneakersshop.model.dto.dictionary.DOrderStatusDto;
import com.umarbariev.sneakersshop.security.UserDetailsServiceImpl;
import com.umarbariev.sneakersshop.service.*;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ShoeModelService shoeModelService;
    private final BrandService brandService;
    private final CategoryService categoryService;

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

    @GetMapping("/data")
    public String getDatabase(Model model) {
        List<ShoeModelInStockInfo> shoeModelInStockInfos = shoeModelService.getShoeModelInStockInfos();
        model.addAttribute("shoesInfo", shoeModelInStockInfos);
        return "admin-shoes-data";
    }

    @GetMapping("data/shoe/edit/{shoeId}")
    public String editShoe(@PathVariable Long shoeId, Model model) {
        EditShoeDto editShoeDto = shoeModelService.getEditShoeDto(shoeId);
        List<DBrandDto> brands = brandService.getAllBrands();
        List<DCategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("editShoe", editShoeDto);
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "admin-edit-shoe";
    }

    @GetMapping("/data/shoe/add")
    public String addNewShoe(Model model) {
        List<DBrandDto> brands = brandService.getAllBrands();
        List<DCategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("editShoe", new EditShoeDto());
        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        return "admin-edit-shoe";
    }

    @PostMapping("/shoe/edit")
    public String saveShoeInfo(@ModelAttribute("editShoe") EditShoeDto editShoeDto) {
        shoeModelService.saveOrUpdateShoe(editShoeDto);
        return "redirect:/admin/data";
    }

    @GetMapping("/shoe/delete/{shoeId}")
    public String deleteShoe(@PathVariable Long shoeId) {
        shoeModelService.deleteShoe(shoeId);
        return "redirect:/admin/data";
    }

    @GetMapping("/brand/add")
    public String addBrandPage(Model model) {
        model.addAttribute("addBrand", new AddBrandDto());
        return "admin-add-brand";
    }

    @PostMapping("/brand/add")
    public String addBrandPage(@ModelAttribute("addBrand") AddBrandDto addBrandDto, HttpServletRequest request) {
        brandService.saveNewBrand(addBrandDto);
        return "redirect:" + request.getHeader("Referer");
    }
}
