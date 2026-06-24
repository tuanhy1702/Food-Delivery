package com.hung.service.sync;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchSync {

    private final FoodSyncService foodSyncService;

    /**
     * Khi ứng dụng Spring Boot khởi động xong,
     * hàm này sẽ được gọi tự động 1 lần để đồng bộ dữ liệu sang Elasticsearch.
     */
    @PostConstruct
    public void sync() {
        log.info("🚀 Bắt đầu khởi tạo đồng bộ dữ liệu Elasticsearch...");
        foodSyncService.syncAllFoodsToElasticsearch();
    }
}