package com.claudiodimauro.Scrape4Engineering.api.services;

import com.claudiodimauro.Scrape4Engineering.api.models.Pattern;
import com.claudiodimauro.Scrape4Engineering.api.repositories.PatternRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatternService {
    @Autowired
    private PatternRepository patternnRepository;
    
    public List<Pattern> getList(){
        return patternnRepository.findAll();
    }
    
    public Optional<Pattern> getById(String id) {
        return patternnRepository.findById(id);
    }

    public Pattern create(Pattern pattern) {
        return patternnRepository.save(pattern);
    }

    public Pattern update(Pattern pattern) {
        return patternnRepository.save(pattern);
    }

    public void delete(String id) {
        patternnRepository.deleteById(id);
    }

    public void deleteAll() {
        patternnRepository.deleteAll();
    }
}
