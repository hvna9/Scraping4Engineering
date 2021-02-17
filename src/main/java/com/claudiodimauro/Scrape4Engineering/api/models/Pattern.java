package com.claudiodimauro.Scrape4Engineering.api.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "pattern")
@ApiModel(description = "")
public class Pattern {

    @ApiModelProperty(notes = "")
    @Id
    private String url;//id = website url
    @ApiModelProperty(notes = "")
    private Boolean hasPreScraping; //verifica se serve il prescraping
    @ApiModelProperty(notes = "")
    private String tagForPreScraping; //può essere null se hasPreScraping == false
    @ApiModelProperty(notes = "")
    private Boolean haveToExplore; //verifica se deve entrare in un articolo (tipo ansa)
    @ApiModelProperty(notes = "")
    private String tagForExploring; //può essere null se haveToExplore == false
    @ApiModelProperty(notes = "")
    private String tagForBody;
    @ApiModelProperty(notes = "")
    private String tagForEntityId;
    @ApiModelProperty(notes = "")
    private String attrForEntityId;
    @ApiModelProperty(notes = "")
    private String tagForEntityTitle;
    @ApiModelProperty(notes = "")
    private Boolean selectorMethodForEntityTitle;//txt true or attr false
    @ApiModelProperty(notes = "")
    private String attrForEntityTitle;//(only to use when the method is attr)
    @ApiModelProperty(notes = "")
    private String entityPath;//(href)
    @ApiModelProperty(notes = "")
    private String lastEntityUpdate;
    @ApiModelProperty(notes = "")
    private String attrLastEntityUpdate;
    @ApiModelProperty(notes = "")
    private String attrForAttachment;
    @ApiModelProperty(notes = "")
    private String tagForContent;

}
