package com.claudiodimauro.Scrape4Engineering.api.services;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.repositories.EntityRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EntityService {

    @Autowired
    EntityRepository entityRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    
//    @Autowired 
//    GridFsOperations gridFsOperations;
//    
//    @Autowired
//    MongoConfig mongoConfig;
    

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

        System.out.println("Save01");
        if (!(entity.getEntityId() == null)) {
            if (entities.isEmpty()) {
                System.out.println("Save02");
                return entityRepository.insert(entity);
            } else {
                System.out.println("Save03");
                for (Entity e : entities) {
                    if (e.getEntityId().equals(entity.getEntityId()) && e.getLastScraping().before(date)) {
                        System.out.println("Save04");
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

    public String findAttachment(String attachmentLink, String entityId, String basePath) {
        try {

            String attachmentOriginalName = attachmentLink.substring(attachmentLink.lastIndexOf("/") + 1);

            URL url = new URL(attachmentLink);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            MultipartFile file = new MockMultipartFile(attachmentOriginalName, attachmentOriginalName, "file/generic", inputStream.readAllBytes());

            /**
             * * DEFINIZIONE DEI METADATA **
             */
            DBObject metadata = new BasicDBObject();
            metadata.put("attachmentOriginalName", attachmentOriginalName);
            metadata.put("size", file.getSize());
            metadata.put("entityId", entityId);
            metadata.put("basePath", basePath);
            
            
            GridFSFile fileFromQuery = gridFsTemplate.findOne(new Query(Criteria.where("metadata.basePath").is(basePath).and("filename").is(attachmentOriginalName)));
            
            if(fileFromQuery != null) {
                return fileFromQuery.getFilename();
            }else {
            gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);
            return file.getOriginalFilename();
            }
        } catch (Exception ex) {
            System.out.println("Impossibile connettersi.");
        }
        return null;
    }
    
    /**EVENTUALMENTE DA IMPLEMENTARE**/
    
//    public String downloadAttachments(String fileId, String localStoragePath) {
//        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoConfig.mongoDbFactory().getDb()); 
//        GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(fileId)));
//        
//        try {
//                File file = new File(localStoragePath + dbFile.getFilename());
//                FileOutputStream streamToDownloadTo = new FileOutputStream(file);
//                gridFSBucket.downloadToStream(dbFile.getId(), streamToDownloadTo);
//                streamToDownloadTo.close();
//            } catch (IOException e) {
//                // handle exception
//                System.out.println("error: " + e.getMessage());
//            } 
//        
//        return null;
//    }
}
