package com.claudiodimauro.Scrape4Engineering.api.controllers;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
import com.claudiodimauro.Scrape4Engineering.api.resources.EntityScraperByPattern;
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

    @GetMapping("/getEntityByTitle")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public List<Entity> getByTitle(@ApiParam(value = "...", required = true) @RequestParam(value = "entityTitle") String entityTitle) {
        return entityService.getByTitle(entityTitle);
    }

    /**
     * * Cambiato da scrapeAndUpload **
     */
    @PostMapping("/scrapeByPattern")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String scrapeByPattern(@RequestBody String patternId) throws Exception {
        EntityScraperByPattern entityScraper = new EntityScraperByPattern(patternId, entityService, patternService);
        String stringScrap = entityScraper.startScraping();
        System.out.println("patternId: " + patternId);
        
//        List<Pattern> patterns = patternService.getList();
//        Pattern pattern = new Pattern();
//        for (Pattern p : patterns) {
//            String url = p.getUrl();
//            if (url.equals(patternId)) {
//                pattern = p;
//            }
//        }
//
//        if (pattern.getUrl() == null) {
//            System.out.println(pattern.getUrl()); 
//            return "Pattern inesistente.";
//        } 
//        else {
//            try {
//                Document doc = Jsoup.connect(pattern.getUrl()).timeout(10000).get();
//
//                Elements entityElements = doc.select(pattern.getTagForBody());
//                for (Element entityElement : entityElements) {
//                    Entity entity = new Entity();
//
//                    Elements idElements = entityElement.select(pattern.getTagForEntityId());
//                    if (!idElements.isEmpty()) {
//                        entity.setEntityId(idElements.get(0).attr(pattern.getAttrForEntityId()));
//                    }
//
//                    Elements titleElements = entityElement.select(pattern.getTagForEntityTitle());
//                    if (!titleElements.isEmpty()) {
//                        if (pattern.getSelectorMethodForEntityTitle() == true) {//vedere se togliere ==
//                            entity.setEntityTitle(titleElements.text());
//                        } else {
//                            entity.setEntityTitle(titleElements.get(0).attr(pattern.getAttrForEntityTitle()));
//                        }
//                    }
//
//                    Elements pathElements = entityElement.select(pattern.getEntityPath());
//                    if (!pathElements.isEmpty()) {
//                        entity.setPath(pathElements.get(0).attr("href"));
//                    }
//                    // veder come fare a convertirlo in tipo date
//                    Elements lastEntityUpdateElements = entityElement.select(pattern.getLastEntityUpdate());
//                    if (!lastEntityUpdateElements.isEmpty()) {
//                        entity.setLastUpdate(lastEntityUpdateElements.attr(pattern.getAttrLastEntityUpdate()));
//                    }
//
//                    Date date = new Date();
//                    entity.setLastScraping(date);//da vedere se inserire nel controllo di inserimento
//
//                    entity.setBasePath(pattern.getUrl());
//
//                    entityService.create(entity);
//                }
//                return "Scraping effettuato con successo.";
//            } catch (IOException ex) {
//                System.out.println("Catturata un'eccezione: \n" + ex.toString());
//            }
//        }

        return "Scraping effettuato --- FASE DI TESTING ---" + stringScrap;
    }

    @PostMapping("/scrapeWithoutPattern")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String scrapeWithoutpattern(@RequestBody Pattern pattern) {

//        //qui serviva una else oltre alla if
//        if (pattern.getUrl() == null) {
//            return "Pattern non valido: impossibile effettuare lo scraping.";
//        } 
//        else {
//            try {
//
//                Document doc = Jsoup.connect(pattern.getUrl()).timeout(10000).get();
//
//                Elements entityElements = doc.select(pattern.getTagForBody());
//                for (Element entityElement : entityElements) {
//                    Entity entity = new Entity();
//
//                    Elements idElements = entityElement.select(pattern.getTagForEntityId());
//                    if (!idElements.isEmpty()) {
//                        entity.setEntityId(idElements.get(0).attr(pattern.getAttrForEntityId()));
//                    }
//
//                    Elements titleElements = entityElement.select(pattern.getTagForEntityTitle());
//                    if (!titleElements.isEmpty()) {
//                        if (pattern.getSelectorMethodForEntityTitle() == true) {//vere se eliminare ==
//                            entity.setEntityTitle(titleElements.text());
//                        } else {
//                            entity.setEntityTitle(titleElements.get(0).attr(pattern.getAttrForEntityTitle()));
//                        }
//                    }
//
//                    Elements pathElements = entityElement.select(pattern.getEntityPath());
//                    if (!pathElements.isEmpty()) {
//                        entity.setPath(pathElements.get(0).attr("href"));
//                    }
//                    // veder come fare a convertirlo in tipo date
//                    Elements lastEntityUpdateElements = entityElement.select(pattern.getLastEntityUpdate());
//                    if (!lastEntityUpdateElements.isEmpty()) {
//                        entity.setLastUpdate(lastEntityUpdateElements.attr(pattern.getAttrLastEntityUpdate()));
//                    }
//
//                    Date date = new Date();
//                    entity.setLastScraping(date);//da vedere se inserire nel controllo di inserimento
//
//                    entity.setBasePath(pattern.getUrl());
//
//                    entityService.create(entity);
//                }
//                patternService.create(pattern);
//
//                return "Scraping effettuato con successo.";
//            } catch (IOException ex) {
//                System.out.println("Catturata un'eccezione: \n" + ex.toString());
//            }
//        }

        return "Scraping effettuato --- FASE DI TESTING ---";
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

    //VALUTARE SE LASCIARE O TOGLIERE
    @PutMapping("/updateEntity/{title}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String updateByTitle(@ApiParam(value = "...", required = true) @PathVariable("title") String title, @RequestBody Entity entity) {
        entityService.update(entity);
        return "La entity \"" + title + "\" è stata aggiornata correttamente.";
    }

    @DeleteMapping("/deleteEntity/{id}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String deleteById(@ApiParam(value = "...", required = true) @PathVariable("id") String id) {
        entityService.delete(id);
        return "La entity con id -> " + id + " <- è stata cancellata correttamente.";
    }

    @DeleteMapping("/deleteEntity/{title}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String deleteByTitle(@ApiParam(value = "...", required = true) @PathVariable("title") String title) {
        entityService.delete(title);
        return "La entity \"" + title + "\" è stata cancellata correttamente.";
    }

    //vedere se lasciare un metodo di pulizia database
    @DeleteMapping("/deleteAllEntity}")
    @ApiOperation(value = "", notes = "", response = Contact.class)
    public String deleteAllEntity() {
        entityService.deleteAll();
        return "Il database è stato svuotato corettamente.";
    }
}
