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
@ApiModel(description = "Componente per effettuare lo scraping degli elementi di cui effettivamente si ha interesse")
public class PatternObject {
    
    
    @ApiModelProperty(notes ="Rappresenta la key su cui verrà mappato l'elemento nel JSON finale che rappresenta l'entità")
    private String elementToScrape;
    @ApiModelProperty(notes = "Rappresenta il tag HTML contente le informazioni di interesse (un testo). Tutto quello estratto tramite questo tag verrà inserito nel campo value del JSON finale che rappresenta l'entità")
    private String tagForElementToScrape;
    @ApiModelProperty(notes = "Specifica il metodo JSoup da utilizzare per effettuare lo scraping. Se true effettua l'utilizzo del metodo text(); se false utilizza il metodo attr(String s).")
    private Boolean methodForElementToScrape; 
    @ApiModelProperty(notes = "È una stringa il cui contenuto rappresenta l'attributo da utilizzare per lo scraping. È null se methodForElementToScrape == true")
    private @Nullable String attrForElementToScrape; //è nullo se methodForElementToScrape==true
}
