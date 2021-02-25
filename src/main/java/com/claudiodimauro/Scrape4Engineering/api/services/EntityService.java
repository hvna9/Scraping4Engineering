package com.claudiodimauro.Scrape4Engineering.api.services;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.repositories.EntityRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
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
//    
//    @Autowired 
//    GridFsOperations gridFsOperations;
    
//    @Autowired
//    MongoConfig mongoConfig;
//    
//    GridFSBucket gridFsBucket;

    

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
}
    
    /**EVENTUALMENTE DA IMPLEMENTARE**/
    
//    public String downloadAttachments(String fileId, String localStoragePath) {
////        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoConfig.mongoDbFactory().getMongoDatabase()); 
////        //GridFSBucket gridFSBucket = GridFSBuckets.create(mongoConfig.mongoDbFactory().getDb(), "fs"); 
////        GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(fileId)));
////        
////        try {
////                File file = new File(localStoragePath + dbFile.getFilename());
////                FileOutputStream streamToDownloadTo = new FileOutputStream(file);
////                gridFSBucket.downloadToStream(dbFile.getId(), streamToDownloadTo);
////                streamToDownloadTo.close();
////            } catch (IOException e) {
////                // handle exception
////                System.out.println("error: " + e.getMessage());
////            } 
////        
//
//
//
//System.out.println("ARRIVO QUIIIII FS");
//
//final String dbURI = "mongodb://root:pippo@localhost:27017";
//
//        MongoClient mongoClient = new MongoClient(new MongoClientURI(dbURI));
//        MongoDatabase database = mongoClient.getDatabase("Scraping4Engineering");
//        
//        System.out.println("E QUIIIII FS ASDNKASDJAKSJDAKSDJNA");
//
//        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
//        gridFSBucket.
//        
//        
//        GridFSFile dbFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(fileId)));
//        
//        System.out.println("E QUIIIII FS");
//        
//        try {
//            FileOutputStream streamToDownloadTo = new FileOutputStream(localStoragePath + dbFile.getFilename());
//            gridFSBucket.downloadToStream(dbFile.getFilename() /*<- original filename*/, streamToDownloadTo);
//            System.out.println("100000000");
//            streamToDownloadTo.close();
//            System.out.println("222222222");
//        } catch (IOException ex) {
//            System.out.println("\n\nFILE NON TROVATO! -> " + ex.toString());
//        } finally {
//            mongoClient.close();
//        }
//        return "hai rotto il cazzo gridfs";
//    }
//
//}
