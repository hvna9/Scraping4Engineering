package com.claudiodimauro.Scrape4Engineering.api.services;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.repositories.EntityRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityService {

    @Autowired
    EntityRepository entityRepository;

    public List<Entity> getList() {
        return entityRepository.findAll();
    }

    public Optional<Entity> getById(String id) {
        return entityRepository.findById(id);
    }

    public List<Entity> getByTitle(String entityTitle) {
        return entityRepository.findByTitle(entityTitle);
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

    /**
     * INSERIRE PARTE SU RETRIEVING DELLE IMMAGINI
     */
}
