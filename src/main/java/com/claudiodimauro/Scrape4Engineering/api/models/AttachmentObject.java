package com.claudiodimauro.Scrape4Engineering.api.models;

import com.mongodb.lang.Nullable;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "")
public class AttachmentObject {
    private String tagForElementToScrape;
    private String attrForElementToScrape; 
}
