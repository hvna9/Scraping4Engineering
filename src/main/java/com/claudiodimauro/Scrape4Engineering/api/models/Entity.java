package com.claudiodimauro.Scrape4Engineering.api.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "entity")
@ApiModel(description = "")
public class Entity {

    @ApiModelProperty(notes = "")
    String entityId;
    @ApiModelProperty(notes = "")
    String entityTitle;
    @ApiModelProperty(notes = "")
    String basePath;
    @ApiModelProperty(notes = "")
    String path;
    @ApiModelProperty(notes = "")
    String url = basePath + path;
    @ApiModelProperty(notes = "")
    String lastUpdate; //convert into Date type
    @ApiModelProperty(notes = "")
    String lastScraping; //convert into Date type
    @ApiModelProperty(notes = "")
    String content;
    @ApiModelProperty(notes = "")
    String[] attachmentId; //convert into ObjectId
    
}
