package com.xavi.swapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int limit;
    private int totalRecords;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
