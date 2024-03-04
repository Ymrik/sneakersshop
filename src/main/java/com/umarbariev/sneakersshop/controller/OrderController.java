package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.Bucket;
import com.umarbariev.sneakersshop.model.dto.CreateOrderDto;
import com.umarbariev.sneakersshop.service.BucketCache;
import com.umarbariev.sneakersshop.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final BucketCache bucketCache;
    private final OrderService orderService;

    @GetMapping("/bucket")
    public String getUserBucket(Model model, Principal principal) {
        Bucket bucket = bucketCache.getBucket(principal.getName());
        model.addAttribute("bucket", bucket);
        return "bucket";
    }

    @GetMapping("/add-to-bucket/{shoeId}")
    public String addToBucket(@PathVariable Long shoeId, Principal principal, HttpServletRequest request) {
        bucketCache.addToBucket(principal.getName(), shoeId);
        String referer = request.getHeader("Referer");
        if (referer.contains("/login")) {
            return "redirect:/";
        }
        return "redirect:" + referer;
    }

    @GetMapping("/bucket/add/{shoeId}")
    public String addShoe(@PathVariable Long shoeId, Principal principal, HttpServletRequest request) {
        bucketCache.addShoeCount(principal.getName(), shoeId, 1L);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/bucket/subtract/{shoeId}")
    public String subtractShoe(@PathVariable Long shoeId, Principal principal, HttpServletRequest request) {
        bucketCache.subtractShoeCount(principal.getName(), shoeId, 1L);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/create")
    public String getCreateOrderPage(Principal principal, Model model) {
        Bucket bucket = bucketCache.getBucket(principal.getName());
        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setBucket(bucket);
        createOrderDto.setUsername(principal.getName());

        model.addAttribute("createOrder", createOrderDto);

        return "create-order";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("createOrder") CreateOrderDto createOrderDto) {
        orderService.createOrder(createOrderDto);
        return "redirect:/client/orders";
    }
}
