package com.claudiodimauro.Scrape4Engineering.api.controllers;

import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
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
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.service.Contact;

@RestController
@RequestMapping("/api")
public class PatternController {

    @Autowired
    private PatternService patternService;

    @GetMapping("/getAllPatterns")
    @ApiOperation(value = "Restituisce tutti i pattern presenti sul DB", notes = "Nessun parametro richiesto", response = Contact.class)
    public List<Pattern> getAll() {
        return patternService.getList();
    }

    @GetMapping("/getPattern/{id}")
    @ApiOperation(value = "Restituisce il pattern che corrisponde all'id passato come parametro", notes = "Fornire un id valido.", response = Contact.class)
    public Pattern getById(@ApiParam(value = "È una stringa che identifica univocamente il pattern su MongoDB", required = true) @PathVariable("id") String id) {
        return patternService.getById(id)
                .orElse(null);
    }

    @PostMapping("/createPattern")
    @ApiOperation(value = "Crea il pattern a partire da un corpo scritto in formato JSON", notes = "Inserire un corpo in un formato JSON valido", response = Contact.class)
    public String create(@ApiParam(value = "È un testo in formato JSON con la struttura del pattern da creare") @RequestBody Pattern pattern) {
        patternService.create(pattern);
        return "Pattern creato correttamente.";
    }

    @PutMapping("/updatePattern/{id}")
    @ApiOperation(value = "Aggiorna il pattern che corrisponde all'id passato come parametro", notes = "Fornire un id valido.", response = Contact.class)
    public String updateById(@ApiParam(value = "È una stringa che identifica univocamente il pattern su MongoDB", required = true)
            @PathVariable("id") String id, @RequestBody Pattern pattern) {
        patternService.update(pattern);
        return "Il pattern " + id + " è stato aggiornato correttamente";
    }

    @DeleteMapping("/deletePattern/{id}")
    @ApiOperation(value = "Cancella il pattern specificato dal parametro Id", notes = "Fornire un id valido", response = Contact.class)
    public String deleteById(@ApiParam(value = "È una stringa che identifica univocamente il pattern su MongoDB", required = true) @PathVariable("id") String id) {
        patternService.delete(id);
        return "Il pattern " + id + " è stato cancellato correttamente";
    }

    @DeleteMapping("/deleteAllPatterns")
    @ApiOperation(value = "Cancella tutte le entità presenti sulla base dati", notes = "Nessun parametro richiesto", response = Contact.class)
    public String deleteAll() {
        patternService.deleteAll();
        return "Tutti i pattern sono stati creati correttamente.";
    }
}
