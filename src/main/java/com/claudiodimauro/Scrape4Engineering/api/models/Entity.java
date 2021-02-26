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
@ApiModel(description = "It is the component that models the information obtained from web sources.")
public class Entity {

    @Id
    @ApiModelProperty(notes = "Identifier assigned by MongoDB. It is only used in the event of an update")
    private String id; 
    @ApiModelProperty(notes = "It is a string with which to identify the entity found. There can be multiple entities with this same id. In combination with basePath it uniquely identifies the resource on the DB.")
    private String entityId;
    @ApiModelProperty(notes = "It is the base address of the source (www.sito.it)")
    private String basePath;
    @ApiModelProperty(notes = "It is the path of the resource on the server (/resource/index.html). Combined with basePath, it returns the complete link to the resource.")
    private String path;
    @ApiModelProperty(notes = "This is the date when the scraping was performed.")
    private Date lastScraping;
    @ApiModelProperty(notes = "It is a DBObject similar to a JSON with key-value structure, which contains all the salient elements extracted as central content of the entity.")
    private DBObject content = new BasicDBObject();
    @ApiModelProperty(notes = "Contains a list of strings representing the ids of all the entity's attachments.")
    private List<ObjectId> attachmentIds = new ArrayList<>();
    @ApiModelProperty(notes = "Represents all additional information for the entity of interest (e.g. title)")
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
