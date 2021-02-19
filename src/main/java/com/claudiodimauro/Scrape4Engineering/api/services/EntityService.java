package com.claudiodimauro.Scrape4Engineering.api.services;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.repositories.EntityRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EntityService {

    @Autowired
    EntityRepository entityRepository;
    
    @Autowired
    private GridFsTemplate gridFsTemplate; 

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
        Date date = new Date(); //data attuale
        List<Entity> entities = getByUrl(url);
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

    public void delete(String id) {
        entityRepository.deleteById(id);
    }

    public void deleteAll() {
        entityRepository.deleteAll();
    }

    public List<Entity> getByUrl(String basePath) {
        return entityRepository.getByUrl(basePath);
    }

    public String findAttachment(String attachmentLink, String entityId) {
        try { 
           
            String attachmentOriginalName = attachmentLink.substring(attachmentLink.lastIndexOf("/") + 1);

            URL url = new URL(attachmentLink);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            
            MultipartFile file = new MockMultipartFile(attachmentOriginalName, attachmentOriginalName, "file/generic", inputStream.readAllBytes());

            /*** DEFINIZIONE DEI METADATA ***/
            DBObject metadata = new BasicDBObject();
            metadata.put("attachmentOriginalName", attachmentOriginalName);
            metadata.put("size", file.getSize());
            metadata.put("entityId", entityId);
            
            gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);
            return file.getOriginalFilename();
        } catch(Exception ex) {
            System.out.println("Impossibile connettersi.");
        }
        return null;
    }
}
