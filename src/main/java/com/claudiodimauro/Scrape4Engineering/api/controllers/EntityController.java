package com.claudiodimauro.Scrape4Engineering.api.controllers;

import com.claudiodimauro.Scrape4Engineering.api.models.Entity;
import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
import com.claudiodimauro.Scrape4Engineering.api.resources.EntityScraperByPattern;
import com.claudiodimauro.Scrape4Engineering.api.resources.EntityScraprerByNewPattern;
import com.claudiodimauro.Scrape4Engineering.api.services.EntityService;
import com.claudiodimauro.Scrape4Engineering.api.services.PatternService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    @GetMapping("/getEntity/all") 
    @ApiOperation(value = "Returns all entities in the homonym collection of DB.", notes = "No params needed.", response = Contact.class)
    public List<Entity> getAll() {
        return entityService.getList();
    }
    
    @GetMapping("/getEntity/{id}") 
    @ApiOperation(value = "Returns the entity corrisponding to the provided id.", notes = "Provides for a valid id.", response = Contact.class)
    public Entity getById(@ApiParam(value = "It's a string that uniquely identify the entity on MongoDB.", required = true) @PathVariable("id") String id) {
        return entityService.getById(id)
                .orElse(null);
    }

    @GetMapping("/getEntityByUrl") 
    @ApiOperation(value = "Returns the entities that the scraping url matches with the passed parameter.", notes = "Provides for a valid url", response = Contact.class)
    public List<Entity> getByUrl(@ApiParam(value = "Is a string that identify the provenience source of the element, excluding the resource link (E.G. www.sito.it)", required = true) @RequestParam(value = "basePath") String basePath) {
        return entityService.getByUrl(basePath);
    }

    @PostMapping("/scrapeByPattern") 
    @ApiOperation(value = "Make the scraping starting from a pattern already existing on DB. It save or update, if the entities exist yet, the retrieved one.", notes = "Provide for a valid id for the pattern you want to use.", response = Contact.class)
    public String scrapeByPattern(@ApiParam(value = "Is a string containing the id of a pattern you want to use.")@RequestBody String patternId) throws Exception {
        EntityScraperByPattern entityScraper = new EntityScraperByPattern(patternId, entityService, patternService);
        return "Scraping done. Results: \n" + entityScraper.startScraping();
    }


    @PostMapping("/scrapeByNewPattern") 
    @ApiOperation(value = "Make the scraping starting from a pattern provided by user and not present yet on the database.", notes = "Insert a body in a valid JSON format.", response = Contact.class)
    public String scrapeByNewPattern(@ApiParam(value = "Is a JSON format text that contains the structure of the pattern you want to use.")@RequestBody Pattern pattern) throws Exception {
        EntityScraprerByNewPattern entityScraper = new EntityScraprerByNewPattern(pattern, entityService, patternService);
        return "Scraping done. Results: \n" + entityScraper.startScraping();
    }
    
    @DeleteMapping("/deleteEntity/all") 
    @ApiOperation(value = "It Deletes all entities on database.", notes = "No params needed.", response = Contact.class)
    public String deleteAllEntity() {
        entityService.deleteAll();
        return "Database succesfully emptied.";
    }

    @DeleteMapping("/deleteEntity/{id}") 
    @ApiOperation(value = "It deletes the entity specified by Id.", notes = "Provides for a valid id.", response = Contact.class)
    public String deleteById(@ApiParam(value = "It's a string that uniquely identify the entity on MongoDB.", required = true) @PathVariable("id") String id) {
        entityService.delete(id);
        return "The entity with id -> " + id + " <- was succesfully deleted.";
    }
    
    @PostMapping("/downloadAttachment/{id}")
    @ApiOperation(value = "Allows to download the specified attachment to a specific floder on your computer.", notes = "Provide for a valid id", response = Contact.class)
    public String downloadAttachment(@ApiParam(value = "It's a string that uniquely identify the attachment on MongoDB.", required = true) @PathVariable("id")
                                     String id, @RequestBody String downloadPath) throws Exception {
        
        return entityService.downloadAttachment(id, downloadPath);
    }
    
    @GetMapping("/showAttachment/{id}")
    @ApiOperation(value = "Show the specified attachment on your browser.", notes = "Provide for a valid id", response = Contact.class)
    public void showAttachment(@ApiParam(value = "It's a string that uniquely identify the attachment on MongoDB.", required = true) @PathVariable("id") String id, HttpServletResponse response) throws Exception {
 
        FileCopyUtils.copy(entityService.showAttachment(id), response.getOutputStream());
    }
}
