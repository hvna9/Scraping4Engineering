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
@ApiModel(description = "Componente per effettuare lo scraping degli allegati associati alle entità")
public class AttachmentObject {
    @ApiModelProperty(notes = "Rappresenta il tag HTML contente le informazioni di interesse (un allegato)")
    private String tagForElementToScrape;
    @ApiModelProperty(notes = "Rappresenta una stringa il cui contenuto rappresenta l'attributo del tag che contiene l'allegato")
    private String attrForElementToScrape; 
}
