package com.claudiodimauro.Scrape4Engineering.api.controllers;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.services.EntityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EntityController {
     @Autowired
    private EntityService entityService;
     
    @GetMapping("/getAllentities")
    public List<Entity> getAll() {
        return entityService.getList();
    }

    @GetMapping("/getEntity/{id}")
    public Entity getById(@PathVariable("id") String id) {
        return entityService.getById(id)
                .orElse(null);
    }

    @GetMapping("/getEntity/{title}")
    public Entity getByTitle(@PathVariable("title") String title) {
        return entityService.getById(title)
                .orElse(null);
    }
    
    /**
     * * Da implementare **
     */
    @PostMapping("/scrapeAndUpload")
    public String scrapeAndUpload() {
        //to define
        return "";
    }
    
    @PostMapping("/createEntity")
    public String create(@RequestBody Entity entity) {
        entityService.create(entity);
        return "La entity è stata creata correttamente.";
    }

    @PutMapping("/updateEntity/{id}")
    public String updateById(@PathVariable("id") String id, @RequestBody Entity entity) {
        entityService.update(entity);
        return "La entity con id " + id + " è stata aggiornata correttamente.";
    }

    @PutMapping("/updateEntity/{title}")
    public String updateByTitle(@PathVariable("title") String title, @RequestBody Entity entity) {
        entityService.update(entity);
        return "La entity \"" + title + "\" è stata aggiornata correttamente.";
    }

    @DeleteMapping("/deleteEntity/{id}")
    public String deleteById(@PathVariable("id") String id) {
        entityService.delete(id);
        return "La entity con id -> " + id + " <- è stata cancellata correttamente.";
    }

    @DeleteMapping("/deleteEntity/{title}")
    public String deleteByTitle(@PathVariable("title") String title) {
        entityService.delete(title);
        return "La entity \"" + title + "\" è stata cancellata correttamente.";
    }
}
