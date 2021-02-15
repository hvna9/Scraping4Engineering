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
    //@ApiOperation
    public List<Pattern> getAll() {
        return patternService.getList();
    }
    
    @GetMapping("/getPattern/{id}")
    //@ApiOperation
    public Pattern getById(@ApiParam(value = "...", required = true) @PathVariable("id") String id) {
        return patternService.getById(id)
                .orElse(null);
    }
    
    @PostMapping("/createPattern")
    //@ApiOperation
    public String create(@RequestBody Pattern pattern) {
        patternService.create(pattern);
        return "Pattern created succesfully.";
    }

    @PutMapping("/updatePattern/{id}")
    //@ApiOperation
    public String updateById(@ApiParam(value = "...", required = true)
    @PathVariable("id") String id, @RequestBody Pattern pattern) {
        patternService.update(pattern);
        return "Pattern " + id + " was upadate succesfully.";
    }
    
    @DeleteMapping("/deletePattern/{id}")
    //@ApiOperation
    public String deleteById(@ApiParam(value = "...", required = true) @PathVariable("id") String id) {
        patternService.delete(id);
        return "The pattern " + id + " was deleted succesfully";
    }

    @DeleteMapping("/deleteAllPatterns")
     //@ApiOperation
    public String deleteAll() {
        patternService.deleteAll();
        return "All patterns was deleted succesfully.";
    }
}
