package com.hung.mapper;
import com.hung.entity.Food;
import com.hung.entity.document.FoodDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface FoodDocumentMapper {
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "shop.name", target = "shopName")
    FoodDocument toFoodDocument(Food food);
}