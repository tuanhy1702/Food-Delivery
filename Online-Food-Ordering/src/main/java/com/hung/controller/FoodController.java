package com.hung.controller;

import com.hung.dto.request.FoodRequest;
import com.hung.dto.response.ApiResponse;
import com.hung.dto.response.FoodResponse;
import com.hung.dto.response.PageResponse;
import com.hung.service.FoodSearchService;
import com.hung.service.FoodService;
import com.hung.service.sync.FoodSyncService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FoodController {
    FoodService foodService;
    FoodSyncService foodSyncService;
    FoodSearchService foodSearchService;
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> createFood(
            @RequestPart("food") FoodRequest foodRequest,
            @RequestPart("images") List<MultipartFile> images
    ) throws IOException {
        foodRequest.setImages(images); // gắn list ảnh vào DTO để service xử lý
        foodService.createFood(foodRequest);
        return ApiResponse.<String>builder()
                .result("Food is created successfully")
                .build();
    }


    @GetMapping("/category/{id}")
    public ApiResponse<List<FoodResponse>> getFoodByCategoryId(@PathVariable String id) {
        return ApiResponse.<List<FoodResponse>>builder()
                .result(foodService.getFoodByCategoryId(id))
                .build();
    }


    @GetMapping("/shop/{id}")
    public ApiResponse<PageResponse<FoodResponse>> getFoodByShopId(@PathVariable String id,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "2") int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return ApiResponse.<PageResponse<FoodResponse>>builder()
                .result(foodService.getFoodByShopId(id, pageable))
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<PageResponse<FoodResponse>> searchFoods(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Lưu lịch sử tìm kiếm (bất đồng bộ)
        foodSyncService.saveHistory(keyword);


        return ApiResponse.<PageResponse<FoodResponse>>builder()
                .result(foodSearchService.searchFoods(keyword, page, size))
                .build();

    }

    @GetMapping("/suggest_search")
    public ApiResponse<List<String >> suggestSearchFoods(
            @RequestParam String keyword
    ) {
        return ApiResponse.<List<String>>builder()
                .result(foodSearchService.suggestFoods(keyword))
                .build();
    }


}
