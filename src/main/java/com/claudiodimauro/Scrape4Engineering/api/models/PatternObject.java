package com.claudiodimauro.Scrape4Engineering.api.models;

import com.mongodb.lang.Nullable;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "")
public class PatternObject {
    private String elementToScrape;
    private String tagForElementToScrape;
    private Boolean methodForElementToScrape; //true -> text(), false -> attr()
    private @Nullable String attrForElementToScrape; //è nullo se methodForElementToScrape==true
}
