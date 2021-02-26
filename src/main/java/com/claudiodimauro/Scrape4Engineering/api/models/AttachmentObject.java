package com.claudiodimauro.Scrape4Engineering.api.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(description = "Component for scraping attachments associated with entities.")
public class AttachmentObject {
    @ApiModelProperty(notes = "Represents the HTML tag containing the information of interest (an attachment).")
    private String tagForElementToScrape;
    @ApiModelProperty(notes = "Represents a string whose content represents the attribute of the tag that contains the attachment.")
    private String attrForElementToScrape; 
}
