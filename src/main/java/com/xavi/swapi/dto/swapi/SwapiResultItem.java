package com.xavi.swapi.dto.swapi;

import lombok.Data;

import java.util.Map;

@Data
public class SwapiResultItem {
    private String uid;
    private String name;
    private String url;
    private Map<String, Object> properties;

    public String getName() {
        if (name != null) {
            return name;
        }
        if (properties != null) {
            if (properties.containsKey("name")) {
                return (String) properties.get("name");
            }
            if (properties.containsKey("title")) {
                return (String) properties.get("title");
            }
        }
        return null;
    }

    public String getUrl() {
        if (url != null) {
            return url;
        }
        if (properties != null && properties.containsKey("url")) {
            return (String) properties.get("url");
        }
        return null;
    }
}
