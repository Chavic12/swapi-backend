package com.xavi.swapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeopleListItem {
    private String uid;
    private String name;
    private String url;
}
