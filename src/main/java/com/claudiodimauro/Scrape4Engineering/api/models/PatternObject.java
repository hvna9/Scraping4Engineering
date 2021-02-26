package com.claudiodimauro.Scrape4Engineering.api.models;

import com.mongodb.lang.Nullable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Component for scraping the elements of which you are actually interested.")
public class PatternObject {
    
    
    @ApiModelProperty(notes ="Represents the key to which the element will be mapped in the final JSON representing the entity.")
    private String elementToScrape;
    @ApiModelProperty(notes = "Represents the HTML tag containing the information of interest (a text). Anything extracted through this tag will be inserted into the value field of the final JSON representing the entity.")
    private String tagForElementToScrape;
    @ApiModelProperty(notes = "Specifies the JSoup method to use for scraping. If true, use the text () method; if false, use the attr (String s) method.")
    private Boolean methodForElementToScrape; 
    @ApiModelProperty(notes = "It is a string whose content represents the attribute to be used for scraping. It is null if methodForElementToScrape == true.")
    private @Nullable String attrForElementToScrape; //è nullo se methodForElementToScrape==true
}
