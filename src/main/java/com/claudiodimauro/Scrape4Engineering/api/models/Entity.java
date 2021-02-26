package com.claudiodimauro.Scrape4Engineering.api.models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "entities")
@ApiModel(description = "È la componente che modella le informazioni ottenute dalle sorgenti Web.")
public class Entity {

    @Id
    @ApiModelProperty(notes = "Identificativo assegnato da MongoDB. Viene utilizzato solo in caso di update")
    private String id; 
    @ApiModelProperty(notes = "È una stringa con cui identificare l'entità trovata. Possono esserci più entità con questo stesso id. In combinazione con basePath identifica univocamente la risorsa sul DB.")
    private String entityId;
    @ApiModelProperty(notes = "È l'indirizzo base della sorgente (www.sito.it)")
    private String basePath;
    @ApiModelProperty(notes = "È il path della risorsa sul server (/risorsa/index.html). Unito al basePath restituisce il link completo alla risorsa.")
    private String path;
    @ApiModelProperty(notes = "È la data di quando è stato effettuato lo scraping.")
    private Date lastScraping;
    @ApiModelProperty(notes = "È un DBObject assimilabile ad un JSON con struttura key-value, che contiene tutti gli elementi salienti estratti come contenuto centrale della entità.")
    private DBObject content = new BasicDBObject();
    @ApiModelProperty(notes = "Contiene una lista di stringhe che rappresentano gli id di tutti gli allegati della entity.")
    private List<ObjectId> attachmentIds = new ArrayList<>();
    @ApiModelProperty(notes = "Rappresenta tutte le informazioni supplementari per l'entità di interesse (Es. titolo)")
    private DBObject entityObject = new BasicDBObject();
    
    public void setEntityObject(String key, String value) {
        this.entityObject.put(key, value);
    }
    
    public void setContent(String key, String value) {
        this.content.put(key, value);
    }
    
    public void setAttachmentIds(ObjectId value) {
        this.attachmentIds.add(value);
    }
}
