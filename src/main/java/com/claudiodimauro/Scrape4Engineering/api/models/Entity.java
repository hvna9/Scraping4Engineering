package com.claudiodimauro.Scrape4Engineering.api.models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "entity")
@ApiModel(description = "")
public class Entity {

    @Id
    String id; //id dato da mongo -> usato solo in caso di update
    @ApiModelProperty(notes = "")
    String entityId;
    @ApiModelProperty(notes = "")
    String basePath;
    @ApiModelProperty(notes = "")
    String path;
    @ApiModelProperty(notes = "")
    Date lastScraping; //convert into Date type
    @ApiModelProperty(notes = "")
    String content; //deve diventare un DBObject
    @ApiModelProperty(notes = "")
    String attachmentId; //da capire come gestire in caso di allegati multipli 
    
    @ApiModelProperty(notes = "")
    DBObject entityObject = new BasicDBObject();
    
    public void setEntityObject(String key, String value) {
        this.entityObject.put(key, value);
    }
    
}
