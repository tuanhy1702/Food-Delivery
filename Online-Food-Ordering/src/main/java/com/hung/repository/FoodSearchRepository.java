package com.hung.repository;

import com.hung.entity.document.FoodDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FoodSearchRepository extends ElasticsearchRepository<FoodDocument, String> {
}