package com.hung.service;

import com.hung.dto.response.FoodOptionGroupResponse;
import com.hung.dto.response.FoodOptionResponse;
import com.hung.dto.response.FoodResponse;
import com.hung.dto.response.PageResponse;
import com.hung.entity.Food;
import com.hung.entity.FoodImage;
import com.hung.entity.FoodOption;
import com.hung.entity.FoodOptionGroup;
import com.hung.entity.document.FoodDocument;
import com.hung.mapper.FoodMapper;
import com.hung.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FoodSearchService {
    ElasticsearchOperations elasticsearchOperations;
    FoodRepository foodRepository;
    FoodMapper foodMapper;

    public PageResponse<FoodResponse> searchFoods(String keyword, int page, int size) {
        // 1️⃣ Query Elasticsearch
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("name", "description")
                                .query(keyword)
                                .fuzziness("AUTO")
                        )
                )
                .withPageable(PageRequest.of(page, size))
                .build();

        SearchHits<FoodDocument> hits = elasticsearchOperations.search(searchQuery, FoodDocument.class);

        // 2️⃣ Lấy ID theo thứ tự Elasticsearch
        List<String> ids = hits.getSearchHits().stream()
                .map(h -> h.getContent().getId())
                .toList();

        if (ids.isEmpty()) {
            return new PageResponse<>(Collections.emptyList(), page, size, 0, 0, true);
        }

        // 3️⃣ Lấy dữ liệu từ DB
        List<Food> foods = foodRepository.findAllById(ids);

        // 4️⃣ Map thứ tự theo Elasticsearch
        Map<String, Integer> order = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) order.put(ids.get(i), i);
        foods.sort(Comparator.comparingInt(f -> order.getOrDefault(f.getId(), Integer.MAX_VALUE)));

        // 5️⃣ Map sang FoodResponse
        List<FoodResponse> responseList = new ArrayList<>();
        for (Food food : foods) {
            FoodResponse response = foodMapper.toFoodResponse(food);

            // Mapping option groups
            for (FoodOptionGroup group : food.getOptionGroup()) {
                FoodOptionGroupResponse groupResponse = foodMapper.toFoodOptionGroupResponse(group);

                // Mapping options
                for (FoodOption option : group.getOptions()) {
                    FoodOptionResponse optionResponse = foodMapper.toFoodOptionResponse(option);
                    groupResponse.getOptions().add(optionResponse);
                }

                response.getOptionGroups().add(groupResponse);
            }

            // Mapping images
            for (FoodImage image : food.getImages()) {
                response.getImages().add(image.getImageUrl());
            }

            responseList.add(response);
        }

        // 6️⃣ Tạo PageResponse
        long totalElements = hits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean last = page + 1 >= totalPages;

        return new PageResponse<>(responseList, page, size, totalElements, totalPages, last);
    }


    public List<String> suggestFoods(String prefix) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .matchPhrasePrefix(m -> m
                                .field("name")
                                .query(prefix.toLowerCase())
                        )
                )
                .withPageable(PageRequest.of(0, 5))
                .build();

        SearchHits<FoodDocument> hits = elasticsearchOperations.search(query, FoodDocument.class);

        return hits.getSearchHits().stream()
                .map(h -> h.getContent().getName())
                .distinct()
                .toList();
    }
}