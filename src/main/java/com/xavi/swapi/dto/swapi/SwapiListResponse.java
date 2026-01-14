package com.xavi.swapi.dto.swapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SwapiListResponse<T> {
    private String message;

    @JsonProperty("total_records")
    private int totalRecords;

    @JsonProperty("total_pages")
    private int totalPages;

    private String next;
    private String previous;
    private List<T> results;
    private List<T> result;

    public List<T> getResults() {
        return results != null ? results : result;
    }
}
