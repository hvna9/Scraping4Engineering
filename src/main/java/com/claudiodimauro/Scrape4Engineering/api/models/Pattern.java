package com.claudiodimauro.Scrape4Engineering.api.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "patterns")
@ApiModel(description = "È quella componente a partire dalla quale è possibile effettuare lo scraping di una sorgente")
public class Pattern {

    @Id
    @ApiModelProperty(notes = "È l'indirizzo della pagina da cui si vogliono ottenere le informazioni. Viene usato come Id perché verosimilmente sarà univoco.")
    private String url;//id = website url
    @ApiModelProperty(notes = "Rappresenta il tag centrale del DOM HTML tramite cui isolare le informazioni di interesse.")
    private String tagForBody;
    @ApiModelProperty(notes = "Rappresenta il tag per estrarre un id locale (non generato da Mongo) con cui identificare l'entità trovata. Possono esserci più entità con questo stesso id.")
    private String entityId;
    @ApiModelProperty(notes = "Rappresenta il tag per otternere il link alla risorsa cercata.")
    private String entityPath;
    @ApiModelProperty(notes = "Rappresenta l'attributo, associato al tag passato tramite entityId, grazie a cui è possibilie estrarre la stringa che farà da Id.")
    private String attrForEntityId;
    @ApiModelProperty(notes = "È una variabile booleana che, se è vera, indica che deve essere effettuato un prescraping poiché le informazioni desiderate saranno strutturate su più pagine.")
    private Boolean hasPrescraping = false;
    @ApiModelProperty(notes = "È il tag che viene utilizzato per effettuare il prescraping, solo quando hasPrescraping == true.")
    private String tagForPrescraping;
    @ApiModelProperty(notes = "È una variabile booleana che, se è vera, indica che il contenuto centrale da estrapolare a partire dal \"click\" su di un link, ossia andandolo a rilevare da un'altra pagina.")
    private Boolean haveToExplore = false;
    @ApiModelProperty(notes = "È una lista contenente un insieme di PatternObject e serve ad estrarre gli elementi \"esterni\" ossia quelli nella pagina puntata dal link base.")
    private List<PatternObject> patternObjects;
    @ApiModelProperty(notes = "È una lista contenente un insieme di PatternObject e serve ad estrarre gli elementi \"interni\" ossia quelli nella pagina interna a partire dall'entità.")
    private List<PatternObject> innerPatternObjects;
    @ApiModelProperty(notes = "Rappresenta il tag centrale del DOM HTML tramite cui isolare le informazioni di interesse nella pagina interna linkata dall'entità")
    private String tagForInnerBody;
    @ApiModelProperty(notes = "È una variabile booleana che, se è vera, indica la presenza di allegati da estrapolare")
    private Boolean hasAttachments = false;
    @ApiModelProperty(notes = "È una lista contenente un insieme di AttachmentObject e serve ad estrarre gli allegati \"esterni\" ossia quelli nella pagina puntata dal link base.")
    private List<AttachmentObject> attachmentObject;
    @ApiModelProperty(notes = "È una variabile booleana che, se è vera, indica la presenza di allegati da estrapolare in una pagina interna al click ci un link.")
    private Boolean hasInnerAttachments = false;
    @ApiModelProperty(notes = "È una lista contenente un insieme di AttachmentObject e serve ad estrarre gli allegati \"interni\" ossia quelli nella pagina interna a partire dall'entità.")
    private List<AttachmentObject> attachmentInnerObject;
    
    public Pattern() {
        this.patternObjects = new ArrayList<>();
        this.innerPatternObjects = new ArrayList<>();
        this.attachmentObject = new ArrayList<>();
        this.attachmentInnerObject = new ArrayList<>();
    }
}
