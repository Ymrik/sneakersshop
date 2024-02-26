package com.umarbariev.sneakersshop.service;

import com.umarbariev.sneakersshop.model.dto.Bucket;
import com.umarbariev.sneakersshop.model.dto.dictionary.DShoeModelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BucketCache {
    private Map<String, Bucket> bucketByUser = new HashMap<>();
    private final ShoeModelService shoeModelService;

    public Bucket getBucket(String username) {
        Bucket bucket = bucketByUser.get(username);
        if (bucket == null) {
            bucket = new Bucket();
            bucketByUser.put(username, bucket);
        }

        return bucket;
    }

    public void addToBucket(String username, Long shoeId) {
        Bucket bucket = getBucket(username);
        DShoeModelDto shoeModelDto = shoeModelService.getShoeById(shoeId);
        bucket.addShoes(shoeModelDto, 1L);
    }

    public void addShoeCount(String username, Long shoeId, Long count) {
        Bucket bucket = getBucket(username);
        DShoeModelDto shoeModelDto = shoeModelService.getShoeById(shoeId);
        bucket.addShoes(shoeModelDto, count);
    }

    public void subtractShoeCount(String username, Long shoeId, Long count) {
        Bucket bucket = getBucket(username);
        DShoeModelDto shoeModelDto = shoeModelService.getShoeById(shoeId);
        bucket.subtractShoes(shoeModelDto, count);
    }
}
