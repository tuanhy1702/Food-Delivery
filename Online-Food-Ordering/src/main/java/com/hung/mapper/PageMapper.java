package com.hung.mapper;

import com.hung.dto.response.PageResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PageMapper {
    PageResponse toPagedResponse(org.springframework.data.domain.Page<?> page);
}
