package com.claudiodimauro.Scrape4Engineering.api.repositories;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface EntityRepository extends MongoRepository<Entity, String>{
    
    @Query(value = "{entityTitle:?0}")
    List<Entity> findByTitle(String entityTitle);
}
