package com.umarbariev.sneakersshop.controller;

import com.umarbariev.sneakersshop.model.dto.Bucket;
import com.umarbariev.sneakersshop.service.BucketCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final BucketCache bucketCache;

    @GetMapping("/bucket")
    public String getUserBucket(Model model, Principal principal) {
        Bucket bucket = bucketCache.getBucket(principal.getName());
        model.addAttribute("bucket", bucket);
        return "bucket";
    }

    @GetMapping("/add-to-bucket/{shoeId}")
    public String addToBucket(@PathVariable Long shoeId, Principal principal, HttpServletRequest request) {
        bucketCache.addToBucket(principal.getName(), shoeId);
        return "redirect:" + request.getHeader("Referer");
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
}
