package com.claudiodimauro.Scrape4Engineering.api.services;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.repositories.EntityRepository;
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

    //SCRIVERE METODO CHE FACCIA UN CONTROLLO PER VEDERE SE L'ID DELL'ENTITA' (NON QUELLO DATO DA MONGO)
    // È GIà PRESENTE O MENO. SE È PRESENTE, AGGIORNARE
    //Si valuti la possibilità di usare save al posto di insert per evitare duplicati
    public Entity create(Entity entity) {
        return entityRepository.insert(entity);
    }

    public Entity update(Entity entity) {
        return entityRepository.save(entity);
    }

    public void delete(String id) {
        entityRepository.deleteById(id);
    }

    public void deleteAll() {
        entityRepository.deleteAll();
    }

    /**
     * INSERIRE PARTE SU RETRIEVING DELLE IMMAGINI
     */
}
