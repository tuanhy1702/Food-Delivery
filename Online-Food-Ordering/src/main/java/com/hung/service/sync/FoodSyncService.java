package com.hung.service.sync;

import com.hung.entity.Food;
import com.hung.entity.SearchHistory;
import com.hung.entity.User;
import com.hung.entity.document.FoodDocument;
import com.hung.repository.FoodRepository;
import com.hung.repository.FoodSearchRepository;
import com.hung.repository.SearchHistoryRepository;
import com.hung.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FoodSyncService {

    FoodRepository foodRepository;
    FoodSearchRepository foodSearchRepository;
    SearchHistoryRepository searchHistoryRepository;
    AuthenticationService authenticationService;

    @Async("syncExecutor")
    public void syncAllFoodsToElasticsearch() {
        log.info("🔄 Bắt đầu đồng bộ dữ liệu Food từ MySQL sang Elasticsearch...");

        List<Food> foods = foodRepository.findAll();

        List<FoodDocument> docs = foods.stream()
                .map(food -> FoodDocument.builder()
                        .id(food.getId())
                        .name(food.getName())
                        .description(food.getDescription())
                        .price(food.getPrice())
                        .available(food.getAvailable())
                        .categoryName(food.getCategory().getName())
                        .shopName(food.getShop().getName())
                        .build()
                ).collect(Collectors.toList());

        foodSearchRepository.saveAll(docs);
        log.info("✅ Đã đồng bộ {} bản ghi Food sang Elasticsearch thành công.", docs.size());
    }

    @Async
    public void saveHistory( String keyword) {
        User user = authenticationService.getUserFromToken();

        SearchHistory history = SearchHistory.builder()
                .user(user)
                .keyword(keyword)
                .createdAt(LocalDateTime.now())
                .build();

        searchHistoryRepository.save(history);
    }
}

