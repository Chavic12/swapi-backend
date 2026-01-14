package com.xavi.swapi.dto;

import com.xavi.swapi.entity.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequest {

    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;

    @NotBlank(message = "Resource ID is required")
    private String resourceId;
}
