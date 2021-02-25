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
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiOperation(value = "Restituisce tutte le entità presenti all'interno della collection \"entities\" del DB.", notes = "Nessun parametro da passare.", response = Contact.class)
    public List<Entity> getAll() {
        return entityService.getList();
    }
    
    @GetMapping("/getEntity/{id}")
    @ApiOperation(value = "Restituisce l'entità che corrisponde all'id passato come parametro", notes = "Fornire un id valido.", response = Contact.class)
    public Entity getById(@ApiParam(value = "È una stringa che identifica univocamente l'entità su MongoDB", required = true) @PathVariable("id") String id) {
        return entityService.getById(id)
                .orElse(null);
    }

    @GetMapping("/getEntityByUrl")
    @ApiOperation(value = "Restituisce le entità che hanno come sito di provenienza l'url passato", notes = "Fornire un url valido", response = Contact.class)
    public List<Entity> getByUrl(@ApiParam(value = "È una stringa che identifica la sorgente di provenienza dell'elemento escluso il link alla risorsa (Es. www.sito.it)", required = true) @RequestParam(value = "basePath") String basePath) {
        return entityService.getByUrl(basePath);
    }

    @PostMapping("/scrapeByPattern")
    @ApiOperation(value = "Effettua lo scraping a partire da un pattern già presente sul DB, andando a salvare o aggiornare -se già presenti- le entità rilevate", notes = "Fornire un id valido per il pattern da utilizzare", response = Contact.class)
    public String scrapeByPattern(@ApiParam(value = "È una stringa contenente l'id del pattern da usare")@RequestBody String patternId) throws Exception {
        EntityScraperByPattern entityScraper = new EntityScraperByPattern(patternId, entityService, patternService);
        return "Scraping done. Results: \n" + entityScraper.startScraping();
    }


    @PostMapping("/scrapeByNewPattern")
    @ApiOperation(value = "Effettua lo scraping a partire da un pattern fornito dall'utente e non presente sulla base dati ", notes = "Inserire un corpo in un formato JSON valido", response = Contact.class)
    public String scrapeByNewPattern(@ApiParam(value = "È un testo in formato JSON con la struttura del pattern da usare")@RequestBody Pattern pattern) throws Exception {
        EntityScraprerByNewPattern entityScraper = new EntityScraprerByNewPattern(pattern, entityService, patternService);
        return "Scraping done. Results: \n" + entityScraper.startScraping();
    }
    
    @DeleteMapping("/deleteEntity/all")
    @ApiOperation(value = "Cancella tutte le entità presenti sulla base dati", notes = "Nessun parametro richiesto", response = Contact.class)
    public String deleteAllEntity() {
        entityService.deleteAll();
        return "Il database è stato svuotato corettamente.";
    }

    @DeleteMapping("/deleteEntity/{id}")
    @ApiOperation(value = "Cancella l'entità specificata dal parametro Id", notes = "Fornire un id valido", response = Contact.class)
    public String deleteById(@ApiParam(value = "È una stringa che identifica univocamente l'entità su MongoDB", required = true) @PathVariable("id") String id) {
        entityService.delete(id);
        return "La entity con id -> " + id + " <- è stata cancellata correttamente.";
    }
//    @GetMapping("/download/{id}")
//    public String download(@PathVariable("id") String id) {
//        entityService.downloadAttachments(id, "/Users/claudiodimauro/Desktop/fileSalvatiDaDB/");
//        return "La salvataggio ok";
//    }
}
