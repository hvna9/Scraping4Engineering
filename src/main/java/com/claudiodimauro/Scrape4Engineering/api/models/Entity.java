package com.claudiodimauro.Scrape4Engineering.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "entity")
public class Entity {
    String entityId;
    String entityTitle;
    String basePath;
    String path;
    String url = basePath + path;
    String lastUpdate; //convert into Date type
    String lastScraping; //convert into Date type
    String content;
    String [] attachmentId; //convert into ObjectId
}