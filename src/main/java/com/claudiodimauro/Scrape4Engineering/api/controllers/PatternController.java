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
    @ApiOperation(value = "Returns all patterns in the homonym collection of DB.", notes = "No params needed.", response = Contact.class)
    public List<Pattern> getAll() {
        return patternService.getList();
    }

    @GetMapping("/getPattern/{id}")
    @ApiOperation(value = "Returns the pattern corrisponding to the provided id.", notes = "Provides for a valid id.", response = Contact.class)
    public Pattern getById(@ApiParam(value = "It's a string that uniquely identify the pattern on MongoDB.", required = true) @PathVariable("id") String id) {
        return patternService.getById(id)
                .orElse(null);
    }

    @PostMapping("/createPattern") 
    @ApiOperation(value = "It's create a pattern starting from a body on JSON file.", notes = "Insert a body in a valid JSON format.", response = Contact.class)
    public String create(@ApiParam(value = "Is a JSON format text that contains the structure of the pattern you want to create.") @RequestBody Pattern pattern) {
        patternService.create(pattern);
        return "Pattern succesfully created.";
    }

    @PutMapping("/updatePattern/{id}")
    @ApiOperation(value = "Updates the pattern corrisponding to provided id.", notes = "Provides for a valid id.", response = Contact.class)
    public String updateById(@ApiParam(value = "It's a string that uniquely identify the pattern on MongoDB.", required = true)
            @PathVariable("id") String id, @RequestBody Pattern pattern) {
        patternService.update(pattern);
        return "The pattern " + id + " was succesfully updated.";
    }

    @DeleteMapping("/deletePattern/{id}")
    @ApiOperation(value = "It deletes the pattern specified by Id.", notes = "Provides for a valid id.", response = Contact.class)
    public String deleteById(@ApiParam(value = "It's a string that uniquely identify the pattern on MongoDB.", required = true) @PathVariable("id") String id) {
        patternService.delete(id);
        return "The pattern " + id + " was succesfully deleted.";
    }

    @DeleteMapping("/deleteAllPatterns")
    @ApiOperation(value = "It Deletes all patterns on database.", notes = "No params needed.", response = Contact.class)
    public String deleteAll() {
        patternService.deleteAll();
        return "All patterns was succesfully deleted.";
    }
}
