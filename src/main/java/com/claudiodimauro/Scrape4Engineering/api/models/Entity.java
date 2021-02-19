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
    Date lastScraping;
    @ApiModelProperty(notes = "")
    DBObject content = new BasicDBObject();
    @ApiModelProperty(notes = "")
    List<String> attachmentIds = new ArrayList<>(); //contiene una lista di stringhe che rappresentano gli id di tutti gli allegati della entity
    @ApiModelProperty(notes = "")
    DBObject entityObject = new BasicDBObject();
    
    public void setEntityObject(String key, String value) {
        this.entityObject.put(key, value);
    }
    
    public void setContent(String key, String value) {
        this.content.put(key, value);
    }
    
    public void setAttachmentIds(String value) {
        this.attachmentIds.add(value);
    }
}
