package com.claudiodimauro.Scrape4Engineering.api.controllers;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
import com.claudiodimauro.Scrape4Engineering.api.resources.EntityScraperByPattern;
import com.claudiodimauro.Scrape4Engineering.api.resources.EntityScraprerWithoutPattern;
import com.claudiodimauro.Scrape4Engineering.api.services.EntityService;
import com.claudiodimauro.Scrape4Engineering.api.services.PatternService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.service.Contact;

@RestController
@RequestMapping("/api")
public class EntityController {

    @Autowired
    private EntityService entityService;
    
    @Autowired
    private PatternService patternService;

    @GetMapping("/getAllentities")
    @ApiOperation(value = "", notes = "", response = Contact.class)//vedere se rimuover Contact.class
    public List<Entity> getAll() {
        return entityService.getList();
    }

    @GetMapping("/getEntity/{id}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public Entity getById(@ApiParam(value = "...", required = true) @PathVariable("id") String id) {
        return entityService.getById(id)
                .orElse(null);
    }
    
    @GetMapping("/getByUrl")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public List<Entity> getByUrl(@ApiParam(value = "...", required = true) @RequestParam(value = "basePath") String basePath) {
        return entityService.getByUrl(basePath);
    }

    @PostMapping("/scrapeByPattern")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String scrapeByPattern(@RequestBody String patternId) throws Exception {
        EntityScraperByPattern entityScraper = new EntityScraperByPattern(patternId, entityService, patternService);
        String stringScrap = entityScraper.startScraping();

        
        return "Scraping effettuato --- FASE DI TESTING ---" + stringScrap;
    }

    @PostMapping("/scrapeWithoutPattern")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String scrapeWithoutpattern(@RequestBody Pattern pattern) throws Exception {
        EntityScraprerWithoutPattern entityScraper = new EntityScraprerWithoutPattern(pattern, entityService, patternService);
        String stringScrap = entityScraper.startScraping();

        return "Scraping effettuato --- FASE DI TESTING ---" + stringScrap;
    }

    //VALUTARE SE LASCIARE O TOGLIERE
    @PostMapping("/createEntity")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String create(@RequestBody Entity entity) {
        entityService.create(entity);
        return "La entity è stata creata correttamente.";
    }

    //VALUTARE SE LASCIARE O TOGLIERE
    @PutMapping("/updateEntity/{id}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String updateById(@ApiParam(value = "...", required = true) @PathVariable("id") String id, @RequestBody Entity entity) {
        entityService.update(entity);
        return "La entity con id " + id + " è stata aggiornata correttamente.";
    }

    @DeleteMapping("/deleteEntity/{id}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String deleteById(@ApiParam(value = "...", required = true) @PathVariable("id") String id) {
        entityService.delete(id);
        return "La entity con id -> " + id + " <- è stata cancellata correttamente.";
    }

    //vedere se lasciare un metodo di pulizia database
    @DeleteMapping("/deleteAllEntity}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String deleteAllEntity() {
        entityService.deleteAll();
        return "Il database è stato svuotato corettamente.";
    }
}
