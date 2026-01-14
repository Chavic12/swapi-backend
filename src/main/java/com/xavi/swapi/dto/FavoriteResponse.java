package com.xavi.swapi.dto;

import com.xavi.swapi.entity.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {

    private Long id;
    private ResourceType resourceType;
    private String resourceId;
    private LocalDateTime createdAt;
    private Object resource;
}
