package com.claudiodimauro.Scrape4Engineering.api.repositories;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository extends MongoRepository<Entity, String>{
    
}
