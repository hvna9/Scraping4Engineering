package com.claudiodimauro.Scrape4Engineering.api.services;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.repositories.EntityRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EntityService {

    @Autowired
    EntityRepository entityRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    
    @Autowired 
    GridFsOperations gridFsOperations;

    public List<Entity> getList() {
        return entityRepository.findAll();
    }

    public Optional<Entity> getById(String id) {
        return entityRepository.findById(id);
    }

    public Entity create(Entity entity) {
        return entityRepository.insert(entity);
    }

    public Entity update(Entity entity) {
        return entityRepository.save(entity);
    }

    public Entity updateScraping(Entity entity, String url) {
        Date date = new Date(); //actual date
        List<Entity> entities = getByUrl(url);

        if (!(entity.getEntityId() == null)) {
            if (entities.isEmpty()) {
                return entityRepository.insert(entity);
            } else {
                for (Entity e : entities) {
                    if (e.getEntityId().equals(entity.getEntityId()) && e.getLastScraping().before(date)) {
                        entity.setId(e.getId());
                        return entityRepository.save(entity);
                    }
                }
            }
            return entityRepository.insert(entity);
        }

        return null;
    }

    public void delete(String id) {
        entityRepository.deleteById(id);
    }

    public void deleteAll() {
        entityRepository.deleteAll();
    }

    public List<Entity> getByUrl(String basePath) {
        return entityRepository.getByUrl(basePath);
    }

    public ObjectId findAttachment(String attachmentLink, String entityId, String basePath) {
        try {

            String attachmentOriginalName = attachmentLink.substring(attachmentLink.lastIndexOf("/") + 1);

            URL url = new URL(attachmentLink);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            MultipartFile file = new MockMultipartFile(attachmentOriginalName, attachmentOriginalName, "file/generic", inputStream.readAllBytes());

            
            //METADATA DEFINITION 
            DBObject metadata = new BasicDBObject();
            metadata.put("attachmentOriginalName", attachmentOriginalName);
            metadata.put("size", file.getSize());
            metadata.put("entityId", entityId);
            metadata.put("basePath", basePath);

            gridFsTemplate.delete(new Query(Criteria.where("metadata.basePath").is(basePath).and("filename").is(attachmentOriginalName)));
            return gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);
            
        } catch (Exception ex) {
            System.out.println("Impossible to connect.");
        }
        return null;
    }

    public String downloadAttachment(String id, String downloadPath) throws Exception {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        
        File file = new File(downloadPath + gridFSFile.getFilename()); 
        FileOutputStream streamToDownloadTo = new FileOutputStream(file); 
        FileCopyUtils.copy(gridFsOperations.getResource(gridFSFile).getInputStream(), streamToDownloadTo);
        
        return "File " + gridFSFile.getFilename() + " succesfully downloaded in " + downloadPath;
       
    }
    
    public InputStream showAttachment(String id) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        return gridFsOperations.getResource(gridFSFile).getInputStream();
    }
}
